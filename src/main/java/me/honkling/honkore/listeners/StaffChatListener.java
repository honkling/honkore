package me.honkling.honkore.listeners;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class StaffChatListener implements Listener {

	private Honkore plugin;

	public StaffChatListener(Honkore plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChatMessage(AsyncPlayerChatEvent e) {
		FileConfiguration config = plugin.getConfig();
		Player p = e.getPlayer();
		String msg = e.getMessage();
		if(p.hasMetadata("staffchat") && p.getMetadata("staffchat").get(0).asBoolean()) {
			e.setCancelled(true);
			Utils.staffChat(p, msg);
		}
	}

}
