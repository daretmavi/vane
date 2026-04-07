package org.oddlama.vane.geyserextension

import org.geysermc.event.subscribe.Subscribe
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCustomItemsEvent
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineResourcePacksEvent
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent
import org.geysermc.geyser.api.event.lifecycle.GeyserPreInitializeEvent
import org.geysermc.geyser.api.extension.Extension
import org.geysermc.geyser.api.item.custom.v2.CustomItemDefinition
import org.geysermc.geyser.api.util.Identifier

/**
 * The main class of your extension - must implement extension, and be in the extension.yml file.
 * See [Extension] for available methods - for example to get the path to the configuration folder.
 */
class Vane : Extension {
    @Subscribe
    fun onGeyserPreInitializeEvent(event: GeyserPreInitializeEvent) {
        val desc = description()
        logger().info("")
        logger().info("##############################################")
        logger().info("Extension: ${desc.name()}")
        logger().info("Version: ${desc.version()}")
        logger().info("Authors: ${desc.authors().joinToString(", ")}")
        logger().info("Description: A plugin-suite that provides many immersive and lore-friendly additions to vanilla Minecraft.")
        logger().info("##############################################")
        logger().info("")
    }

    @Subscribe
    fun onGeyserDefineCustomItems(event: GeyserDefineCustomItemsEvent) {
        // Register the custom tome models. Map the Java base items (book / enchanted_book)
        // to our resource pack model identifiers so Bedrock clients see the correct textures.
        // Non-enchanted tomes use the normal book item, enchanted variants use enchanted_book.

        // Ancient Tome (normal book)
        event.register(
            Identifier.of("book"),
            CustomItemDefinition.builder(
                Identifier.of("vane_enchantments:item/ancient_tome"),
                Identifier.of("vane_enchantments:item/ancient_tome")
            ).build()
        )

        // Enchanted Ancient Tome (enchanted_book)
        event.register(
            Identifier.of("enchanted_book"),
            CustomItemDefinition.builder(
                Identifier.of("vane_enchantments:item/enchanted_ancient_tome"),
                Identifier.of("vane_enchantments:item/ancient_tome")
            ).build()
        )

        // Ancient Tome of Knowledge (normal book)
        event.register(
            Identifier.of("book"),
            CustomItemDefinition.builder(
                Identifier.of("vane_enchantments:item/ancient_tome_of_knowledge"),
                Identifier.of("vane_enchantments:item/ancient_tome_of_knowledge")
            ).build()
        )

        // Enchanted Ancient Tome of Knowledge (enchanted_book)
        event.register(
            Identifier.of("enchanted_book"),
            CustomItemDefinition.builder(
                Identifier.of("vane_enchantments:item/enchanted_ancient_tome_of_knowledge"),
                Identifier.of("vane_enchantments:item/ancient_tome_of_knowledge")
            ).build()
        )

        // Ancient Tome of the Gods (normal book)
        event.register(
            Identifier.of("book"),
            CustomItemDefinition.builder(
                Identifier.of("vane_enchantments:item/ancient_tome_of_the_gods"),
                Identifier.of("vane_enchantments:item/ancient_tome_of_the_gods")
            ).build()
        )

        // Enchanted Ancient Tome of the Gods (enchanted_book)
        event.register(
            Identifier.of("enchanted_book"),
            CustomItemDefinition.builder(
                Identifier.of("vane_enchantments:item/enchanted_ancient_tome_of_the_gods"),
                Identifier.of("vane_enchantments:item/ancient_tome_of_the_gods")
            ).build()
        )

        // ----- Vane Trifles items -----
        // Sickles (mapped to the corresponding hoe base item)
        event.register(
            Identifier.of("wooden_hoe"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/wooden_sickle"),
                Identifier.of("vane_trifles:item/wooden_sickle")
            ).build()
        )

        event.register(
            Identifier.of("stone_hoe"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/stone_sickle"),
                Identifier.of("vane_trifles:item/stone_sickle")
            ).build()
        )

        event.register(
            Identifier.of("iron_hoe"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/iron_sickle"),
                Identifier.of("vane_trifles:item/iron_sickle")
            ).build()
        )

        event.register(
            Identifier.of("golden_hoe"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/golden_sickle"),
                Identifier.of("vane_trifles:item/golden_sickle")
            ).build()
        )

        event.register(
            Identifier.of("diamond_hoe"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/diamond_sickle"),
                Identifier.of("vane_trifles:item/diamond_sickle")
            ).build()
        )

        event.register(
            Identifier.of("netherite_hoe"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/netherite_sickle"),
                Identifier.of("vane_trifles:item/netherite_sickle")
            ).build()
        )

        // Pouch (mapped to dropper)
        event.register(
            Identifier.of("dropper"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/pouch"),
                Identifier.of("vane_trifles:item/pouch")
            ).build()
        )

        // Backpack (mapped to shulker_box)
        event.register(
            Identifier.of("shulker_box"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/backpack"),
                Identifier.of("vane_trifles:item/backpack")
            ).build()
        )

        // Slime buckets (mapped to slime_ball)
        event.register(
            Identifier.of("slime_ball"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/slime_bucket"),
                Identifier.of("vane_trifles:item/slime_bucket")
            ).build()
        )

        event.register(
            Identifier.of("slime_ball"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/slime_bucket_excited"),
                Identifier.of("vane_trifles:item/slime_bucket_excited")
            ).build()
        )

        // Reinforced Elytra (mapped to elytra)
        event.register(
            Identifier.of("elytra"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/reinforced_elytra"),
                Identifier.of("vane_trifles:item/reinforced_elytra")
            ).build()
        )

        // Compass variant (north compass)
        event.register(
            Identifier.of("compass"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/north_compass"),
                Identifier.of("vane_trifles:item/north_compass")
            ).build()
        )

        // Papyrus scroll (paper)
        event.register(
            Identifier.of("paper"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/papyrus_scroll"),
                Identifier.of("vane_trifles:item/papyrus_scroll")
            ).build()
        )

        // Parchment / home/unstable/file/trowel/spawn/lodestone/death scrolls (warped_fungus_on_a_stick)
        event.register(
            Identifier.of("warped_fungus_on_a_stick"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/home_scroll"),
                Identifier.of("vane_trifles:item/home_scroll")
            ).build()
        )
        event.register(
            Identifier.of("warped_fungus_on_a_stick"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/unstable_scroll"),
                Identifier.of("vane_trifles:item/unstable_scroll")
            ).build()
        )
        event.register(
            Identifier.of("warped_fungus_on_a_stick"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/file"),
                Identifier.of("vane_trifles:item/file")
            ).build()
        )
        event.register(
            Identifier.of("warped_fungus_on_a_stick"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/trowel"),
                Identifier.of("vane_trifles:item/trowel")
            ).build()
        )
        event.register(
            Identifier.of("warped_fungus_on_a_stick"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/spawn_scroll"),
                Identifier.of("vane_trifles:item/spawn_scroll")
            ).build()
        )
        event.register(
            Identifier.of("warped_fungus_on_a_stick"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/lodestone_scroll"),
                Identifier.of("vane_trifles:item/lodestone_scroll")
            ).build()
        )
        event.register(
            Identifier.of("warped_fungus_on_a_stick"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/death_scroll"),
                Identifier.of("vane_trifles:item/death_scroll")
            ).build()
        )

        // Parchment bag (paper already mapped above) and other items
        event.register(
            Identifier.of("glass_bottle"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/empty_xp_bottle"),
                Identifier.of("vane_trifles:item/empty_xp_bottle")
            ).build()
        )

        // XP bottle sizes (honey_bottle)
        event.register(
            Identifier.of("honey_bottle"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/small_xp_bottle"),
                Identifier.of("vane_trifles:item/small_xp_bottle")
            ).build()
        )
        event.register(
            Identifier.of("honey_bottle"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/medium_xp_bottle"),
                Identifier.of("vane_trifles:item/medium_xp_bottle")
            ).build()
        )
        event.register(
            Identifier.of("honey_bottle"),
            CustomItemDefinition.builder(
                Identifier.of("vane_trifles:item/large_xp_bottle"),
                Identifier.of("vane_trifles:item/large_xp_bottle")
            ).build()
        )
    }


    /**
     * Registering custom items/blocks, or adding resource packs (and basically all other events that are fired before Geyser initializes fully)
     * are done in their respective events. See below for an example:
     */
    @Subscribe
    fun onGeyserDefineResourcePacksEvent(event: GeyserDefineResourcePacksEvent) {
        logger().info("Loading: ${event.resourcePacks().size} resource packs.")
    }

    /**
     * You can use the GeyserPostInitializeEvent to run anything after Geyser fully initialized and is ready to accept bedrock player connections.
     */
    @Subscribe
    fun onPostInitialize(event: GeyserPostInitializeEvent?) {
        with(logger()) {
            info("Loading ${description().name()}...")
            info("${dataFolder()}")
        }
    }

    /**
     * You can reload your extension - for example, reload the extension config - by listening to Geyser's
     * [org.geysermc.geyser.api.event.lifecycle.GeyserPreReloadEvent]
     */
    @Subscribe
    fun onGeyserReload(event: GeyserPreInitializeEvent?) {
        logger().info("Reloading ${description().name()}!")
    }
}
