package org.oddlama.vane.geyserextension


/**
 * The main class of your extension - must implement extension, and be in the extension.yml file.
 * See [Extension] for available methods - for example to get the path to the configuration folder.
 */
import org.geysermc.event.subscribe.Subscribe
import org.geysermc.geyser.api.event.lifecycle.*
import org.geysermc.geyser.api.extension.Extension

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

    @Subscribe
    fun onGeyserDefineCommands(event: GeyserDefineCommandsEvent) {
        CommandsRegistration.onGeyserDefineCommands(event, this)
    }

    @Subscribe
    fun onGeyserDefineCustomItems(event: GeyserDefineCustomItemsEvent) {
        ItemRegistration.onGeyserDefineCustomItems(event)
    }
}
