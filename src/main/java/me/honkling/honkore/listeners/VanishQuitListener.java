package me.honkling.honkore.listeners;

import me.honkling.honkore.Honkore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class VanishQuitListener implements Listener {

	private final Honkore plugin = Honkore.getInstance();

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e) {
		Player p = e.getPlayer();
		p.removeMetadata("vanish", plugin);

		for(Player member : Bukkit.getOnlinePlayers()) {
			p.showPlayer(plugin, member);
			member.showPlayer(plugin, p);
		}
	}

}
