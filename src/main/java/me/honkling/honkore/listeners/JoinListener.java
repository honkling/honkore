package me.honkling.honkore.listeners;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

	private final Honkore plugin = Honkore.getInstance();

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		if(plugin.getConfig().getBoolean("vanish-system")) {
			Player executor = e.getPlayer();

			if (!executor.hasPermission("honkore.vanish"))
				return;

			for (Player player : Bukkit.getOnlinePlayers()) {
				if (executor.hasMetadata("vanish"))
					if (executor.getMetadata("vanish").get(0).asBoolean())
						player.hidePlayer(plugin, executor);
			}
		}
		if(plugin.getConfig().getBoolean("join-leave")) {
			String message = plugin.getConfig().getString("Messages.join-message");

			if(message == null) {
				plugin.getLogger().warning("Join message has not been defined. Skipping...");
				return;
			}

			Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
			component = Utils.translate(component, "\\{DISPLAYNAME\\}", LegacyComponentSerializer.legacyAmpersand().serialize(e.getPlayer().displayName()));

			e.joinMessage(component);
		}
	}

}
