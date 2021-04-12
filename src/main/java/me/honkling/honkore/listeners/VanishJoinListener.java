package me.honkling.honkore.listeners;

import me.honkling.honkore.Honkore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class VanishJoinListener implements Listener {

	private final Honkore plugin = Honkore.getInstance();

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		Player executor = e.getPlayer();

		if (!executor.hasPermission("honkore.vanish"))
			return;

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (executor.hasMetadata("vanish"))
				if (executor.getMetadata("vanish").get(0).asBoolean())
					player.hidePlayer(plugin, executor);
		}
	}

}
