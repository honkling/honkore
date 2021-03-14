package me.honkling.honkore.listeners;

import me.honkling.honkore.Honkore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collection;

public class ChatListener implements Listener {

	private Honkore plugin;

	public ChatListener(Honkore plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChatMessage(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		if(p.hasMetadata("staffchat") && p.getMetadata("staffchat").get(0).asBoolean()) {
			e.setCancelled(true);
			Collection<? extends Player> staff = Bukkit.getOnlinePlayers();
			for(Player member : staff) {
				if(member.hasPermission("staff.honkore")) {
					member.sendMessage(String.format("§d[STAFF] §7%s: %s", member.getName(), msg));
				}
			}
		} else {

		}
	}

}
