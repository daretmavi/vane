package org.oddlama.vane.regions.region;

import static org.oddlama.vane.core.persistent.PersistentSerializer.from_json;
import static org.oddlama.vane.core.persistent.PersistentSerializer.to_json;

import static org.oddlama.vane.util.BlockUtil.adjacent_blocks_3d;
import static org.oddlama.vane.util.BlockUtil.unpack;
import static org.oddlama.vane.util.ItemUtil.name_item;
import static org.oddlama.vane.util.Nms.item_handle;
import static org.oddlama.vane.util.Nms.register_entity;
import static org.oddlama.vane.util.Nms.spawn;
import static org.oddlama.vane.util.Util.ms_to_ticks;
import static org.oddlama.vane.util.Util.namespaced_key;

import org.oddlama.vane.external.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumCreatureType;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.oddlama.vane.annotation.VaneModule;
import org.oddlama.vane.annotation.config.ConfigExtendedMaterial;
import org.oddlama.vane.annotation.config.ConfigLong;
import org.oddlama.vane.annotation.config.ConfigMaterialMapEntry;
import org.oddlama.vane.annotation.config.ConfigMaterialMapMapEntry;
import org.oddlama.vane.annotation.config.ConfigMaterialMapMapMap;
import org.oddlama.vane.annotation.config.ConfigMaterialMapMapMapEntry;
import org.oddlama.vane.annotation.lang.LangMessage;
import org.oddlama.vane.annotation.persistent.Persistent;
import org.oddlama.vane.core.functional.Consumer2;
import org.oddlama.vane.core.lang.TranslatedMessage;
import org.oddlama.vane.core.material.ExtendedMaterial;
import org.oddlama.vane.core.module.Module;
import org.oddlama.vane.core.persistent.PersistentSerializer;
import org.oddlama.vane.util.LazyBlock;
import org.oddlama.vane.regions.Regions;

public class RegionGroup {
	public static Object serialize(@NotNull final Object o) throws IOException {
		final var region_group = (RegionGroup)o;
		final var json = new JSONObject();
		json.put("id",                 to_json(UUID.class,            region_group.id));
		json.put("name",               to_json(String.class,          region_group.name));
		json.put("owner",              to_json(UUID.class,            region_group.owner));
		try {
			json.put("roles",          to_json(RegionGroup.class.getDeclaredField("roles"), region_group.roles));
		} catch (NoSuchFieldException e) { throw new RuntimeException("Invalid field. This is a bug.", e); }
		try {
			json.put("player_to_role", to_json(RegionGroup.class.getDeclaredField("player_to_role"), region_group.player_to_role));
		} catch (NoSuchFieldException e) { throw new RuntimeException("Invalid field. This is a bug.", e); }
		json.put("role_others",        to_json(UUID.class,            region_group.role_others));
		try {
			json.put("settings",       to_json(RegionGroup.class.getDeclaredField("settings"), region_group.settings));
		} catch (NoSuchFieldException e) { throw new RuntimeException("Invalid field. This is a bug.", e); }

		return json;
	}

	@SuppressWarnings("unchecked")
	public static RegionGroup deserialize(@NotNull final Object o) throws IOException {
		final var json = (JSONObject)o;
		final var region_group = new RegionGroup();
		region_group.id                 = from_json(UUID.class,            json.get("id"));
		region_group.name               = from_json(String.class,          json.get("name"));
		region_group.owner              = from_json(UUID.class,            json.get("owner"));
		try {
			region_group.roles          = (Map<UUID, Role>)from_json(RegionGroup.class.getDeclaredField("roles"), json.get("roles"));
		} catch (NoSuchFieldException e) { throw new RuntimeException("Invalid field. This is a bug.", e); }
		try {
			region_group.player_to_role = (Map<UUID, UUID>)from_json(RegionGroup.class.getDeclaredField("player_to_role"), json.get("player_to_role"));
		} catch (NoSuchFieldException e) { throw new RuntimeException("Invalid field. This is a bug.", e); }
		region_group.role_others        = from_json(UUID.class,            json.get("role_others"));
		try {
			region_group.settings       = (Map<EnvironmentSetting, Boolean>)from_json(RegionGroup.class.getDeclaredField("settings"), json.get("settings"));
		} catch (NoSuchFieldException e) { throw new RuntimeException("Invalid field. This is a bug.", e); }
		return region_group;
	}

	private UUID id;
	private String name;
	private UUID owner;

	private Map<UUID, Role> roles = new HashMap<>();
	private Map<UUID, UUID> player_to_role = new HashMap<>();
	private UUID role_others;

	private Map<EnvironmentSetting, Boolean> settings = new HashMap<>();

	private RegionGroup() { }
	private RegionGroup(final String name, final UUID owner) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.owner = owner;

		// Add admins role
		final var admins = new Role("[admins]", Role.RoleType.ADMINS);
		this.add_role(admins);

		// Add others role
		final var others = new Role("[others]", Role.RoleType.OTHERS);
		this.add_role(others);
		this.role_others = others.id();

		// Add owner to admins
		this.player_to_role.put(owner, admins.id());

		// Set setting defaults
		for (var es : EnvironmentSetting.values()) {
			this.settings.put(es, es.default_value());
		}
	}

	public UUID id() { return id; }
	public String name() { return name; }
	public UUID owner() { return owner; }
	public boolean get_setting(final EnvironmentSetting setting) {
		return settings.getOrDefault(setting, setting.default_value());
	}

	public void add_role(final Role role) {
		this.roles.put(role.id(), role);
	}

	public Role get_role(final UUID player) {
		return roles.get(player_to_role.getOrDefault(player, role_others));
	}
}
