package org.oddlama.vane.core.itemv2;

import static org.oddlama.vane.util.MaterialUtil.is_tillable;
import static org.oddlama.vane.util.Nms.item_handle;

import org.bukkit.Keyed;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.SmithingRecipe;
import org.oddlama.vane.core.Core;
import org.oddlama.vane.core.Listener;
import org.oddlama.vane.core.itemv2.api.CustomItem;
import org.oddlama.vane.core.itemv2.api.InhibitBehavior;
import org.oddlama.vane.core.module.Context;

// TODO recipe book click event
public class VanillaFunctionalityInhibitor extends Listener<Core> {
	public VanillaFunctionalityInhibitor(Context<Core> context) {
		super(context);
	}

	private boolean inhibit(CustomItem custom_item, InhibitBehavior behavior) {
		return custom_item != null && custom_item.enabled() && custom_item.inhibitedBehaviors().contains(behavior);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void on_pathfind(final EntityTargetEvent event) {
		if (event.getReason() != EntityTargetEvent.TargetReason.TEMPT) {
			return;
		}

		if (event.getTarget() instanceof Player player) {
			final var custom_item_main = get_module().item_registry().get(player.getInventory().getItemInMainHand());
			final var custom_item_off = get_module().item_registry().get(player.getInventory().getItemInOffHand());

			if (inhibit(custom_item_main, InhibitBehavior.TEMPT) || inhibit(custom_item_off, InhibitBehavior.TEMPT)) {
				event.setCancelled(true);
			}
		}
	}

	// Prevent custom hoe items from tilling blocks
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void on_player_hoe_right_click_block(final PlayerInteractEvent event) {
		if (!event.hasBlock() || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		// Only when clicking a tillable block
		if (!is_tillable(event.getClickedBlock().getType())) {
			return;
		}

		final var player = event.getPlayer();
		final var item = player.getEquipment().getItem(event.getHand());
		if (inhibit(get_module().item_registry().get(item), InhibitBehavior.HOE_TILL)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void on_prepare_item_craft(final PrepareItemCraftEvent event) {
		final var recipe = event.getRecipe();
		if (recipe == null || !(recipe instanceof Keyed keyed)) {
			return;
		}

		// Only consider to cancel minecraft's recipes
		if (!keyed.getKey().getNamespace().equals("minecraft")) {
			return;
		}

		for (final var item : event.getInventory().getMatrix()) {
			if (inhibit(get_module().item_registry().get(item), InhibitBehavior.USE_IN_VANILLA_RECIPE)) {
				event.getInventory().setResult(null);
				return;
			}
		}
	}

	// Prevent custom items from being used in smiting by default. They have to override this event to allow it.
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void on_prepare_smithing(final PrepareSmithingEvent event) {
		final var item = event.getInventory().getInputEquipment();
		final var recipe = event.getInventory().getRecipe();
		if (recipe == null || !(recipe instanceof Keyed keyed)) {
			return;
		}

		// Only consider to cancel minecraft's recipes
		if (!keyed.getKey().getNamespace().equals("minecraft")) {
			return;
		}

		if (inhibit(get_module().item_registry().get(item), InhibitBehavior.USE_IN_VANILLA_RECIPE)) {
			event.getInventory().setResult(null);
		}
	}

	// If the result of a smithing recipe is a custom item, copy and merge input NBT data.
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void on_prepare_smithing_copy_nbt(final PrepareSmithingEvent event) {
		var result = event.getResult();
		final var recipe = event.getInventory().getRecipe();
		if (result == null || recipe == null || !(recipe instanceof SmithingRecipe smithing_recipe) || !smithing_recipe.willCopyNbt()) {
			return;
		}

        // Actually use recipe result, as copynbt has already modified the result
		result = recipe.getResult();
		final var custom_item_result = get_module().item_registry().get(result);
		if (custom_item_result == null) {
			return;
		}

		final var input = event.getInventory().getInputEquipment();
		final var input_nbt = CraftItemStack.asNMSCopy(input).getOrCreateTag();
		final var result_nbt = CraftItemStack.asNMSCopy(result).getOrCreateTag();
		final var nms_result = item_handle(result).copy();
		nms_result.setTag(result_nbt.merge(input_nbt));
		event.setResult(custom_item_result.convertExistingStack(CraftItemStack.asCraftMirror(nms_result)));
	}

	// TODO: what about inventory based item repair?
	// Always prevent custom item repair with the custom item base material
	// if it is not also a matching custom item.
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void on_prepare_anvil(final PrepareAnvilEvent event) {
		final var a = event.getInventory().getFirstItem();
		final var b = event.getInventory().getSecondItem();

		// If both items are not of the same material, there is nothing to do.
		if (a == null || b == null || a.getType() != b.getType()) {
			return;
		}

		// Disable the result unless a and b are instances of the same custom item.
		final var custom_item_a = get_module().item_registry().get(a);
		final var custom_item_b = get_module().item_registry().get(b);
		if (custom_item_a != null && custom_item_a != custom_item_b) {
			event.setResult(null);
		}
	}

	// Prevent netherite items from burning, as they are made of netherite
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void on_item_burn(final EntityDamageEvent event) {
		// Only burn damage on dropped items
		if (event.getEntity().getType() != EntityType.DROPPED_ITEM) {
			return;
		}

		switch (event.getCause()) {
			default:
				return;
			case FIRE:
			case FIRE_TICK:
			case LAVA:
				break;
		}

		final var entity = event.getEntity();
		if (!(entity instanceof Item)) {
			return;
		}

		final var item = ((Item)entity).getItemStack();
		if (inhibit(get_module().item_registry().get(item), InhibitBehavior.ITEM_BURN)) {
			event.setCancelled(true);
		}
	}
}
