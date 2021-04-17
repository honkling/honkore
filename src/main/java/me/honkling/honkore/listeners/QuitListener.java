package me.honkling.honkore.listeners;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

	private final Honkore plugin = Honkore.getInstance();

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e) {
		if(plugin.config.getBoolean("vanish-system")) {
			Player p = e.getPlayer();
			p.removeMetadata("vanish", plugin);

			for (Player member : Bukkit.getOnlinePlayers()) {
				member.showPlayer(plugin, p);
			}
		}
		if(plugin.config.getBoolean("join-leave")) {
			String message = plugin.config.getString("Messages.quit-message");

			if(message == null) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cQuit message has not been defined. Please contact an admin.");
				e.getPlayer().sendMessage(component);
				return;
			}

			Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
			component = Utils.translate(component, "\\{DISPLAYNAME\\}", LegacyComponentSerializer.legacyAmpersand().serialize(e.getPlayer().displayName()));

			e.quitMessage(component);
		}
	}

}
