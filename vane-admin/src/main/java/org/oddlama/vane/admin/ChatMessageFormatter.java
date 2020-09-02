package org.oddlama.vane.admin;

import static org.oddlama.vane.util.WorldUtil.broadcast;

import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.oddlama.vane.annotation.VaneModule;
import org.oddlama.vane.annotation.lang.LangMessage;
import org.oddlama.vane.annotation.persistent.Persistent;
import org.oddlama.vane.core.module.Module;
import org.oddlama.vane.core.persistent.PersistentLocation;
import org.oddlama.vane.util.Message;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import org.oddlama.vane.annotation.config.ConfigBoolean;
import org.oddlama.vane.annotation.config.ConfigStringList;
import org.oddlama.vane.annotation.lang.LangMessage;
import org.oddlama.vane.core.Listener;
import org.oddlama.vane.core.module.Context;
import org.oddlama.vane.util.Message;

public class ChatMessageFormatter extends Listener<Admin> {
	// Language
	@LangMessage
	private Message lang_player_chat_format;
	@LangMessage
	private Message lang_player_join;
	@LangMessage
	private Message lang_player_kick;
	@LangMessage
	private Message lang_player_quit;

	public ChatMessageFormatter(Context<Admin> context) {
		super(context);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on_player_chat(AsyncPlayerChatEvent event) {
		// TODO color based on privilege or config value.... somehow
		// link permission groups to different config values....
		String color = "§b";
		event.setFormat(lang_player_chat_format.format(color, "%1$s", "%2$s"));
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void on_player_join(final PlayerJoinEvent event) {
		event.setJoinMessage(lang_player_join.format(event.getPlayer().getPlayerListName()));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on_player_kick(final PlayerKickEvent event) {
		event.setLeaveMessage(lang_player_kick.format(event.getPlayer().getPlayerListName()));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on_player_quit(final PlayerQuitEvent event) {
		event.setQuitMessage(lang_player_quit.format(event.getPlayer().getPlayerListName()));
	}
}
