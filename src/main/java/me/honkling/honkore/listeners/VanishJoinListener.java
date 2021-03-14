package me.honkling.honkore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class VanishJoinListener implements Listener {

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Plugin plugin = Bukkit.getPluginManager().getPlugin("honkore");
		assert plugin != null;
		if(!p.hasPermission("honkore.vanish")) {
			for (Player member : Bukkit.getOnlinePlayers()) {
				if (member != e.getPlayer() && member.hasMetadata("vanish") && member.getMetadata("vanish").get(0).asBoolean()) {
					p.hidePlayer(plugin, member);
				}
			}
		}
	}

}
