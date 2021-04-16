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
		if(plugin.getConfig().getBoolean("vanish-system")) {
			Player p = e.getPlayer();
			p.removeMetadata("vanish", plugin);

			for (Player member : Bukkit.getOnlinePlayers()) {
				member.showPlayer(plugin, p);
			}
		}
		if(plugin.getConfig().getBoolean("join-leave")) {
			String message = plugin.getConfig().getString("Messages.quit-message");

			if(message == null) {
				plugin.getLogger().warning("Quit message has not been defined. Skipping...");
				return;
			}

			Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
			component = Utils.translate(component, "\\{DISPLAYNAME\\}", LegacyComponentSerializer.legacyAmpersand().serialize(e.getPlayer().displayName()));

			e.quitMessage(component);
		}
	}

}
