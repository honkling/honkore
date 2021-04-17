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
		if(plugin.config.getBoolean("vanish-system")) {
			Player executor = e.getPlayer();

			if (executor.hasPermission("honkore.vanish")) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (executor.hasMetadata("vanish"))
						if (executor.getMetadata("vanish").get(0).asBoolean())
							player.hidePlayer(plugin, executor);
				}
			}
		}
		if(plugin.config.getBoolean("join-leave")) {
			String message = plugin.config.getString("Messages.join-message");

			if(message == null) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cJoin message has not been defined. Please contact an admin.");
				e.getPlayer().sendMessage(component);
				return;
			}

			Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
			component = Utils.translate(component, "\\{DISPLAYNAME\\}", LegacyComponentSerializer.legacyAmpersand().serialize(e.getPlayer().displayName()));

			e.joinMessage(component);
		}
	}

}
