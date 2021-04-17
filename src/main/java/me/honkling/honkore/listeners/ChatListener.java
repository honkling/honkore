package me.honkling.honkore.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.ChatComposer;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;

public class ChatListener implements Listener {

	private final Honkore plugin = Honkore.getInstance();

	@EventHandler
	public void onChatMessage(AsyncChatEvent e) {
		Player p = e.getPlayer();
		if(plugin.config.getBoolean("chat-tools")) {
			if (plugin.chatMuted && !p.hasPermission("honkore.bypasschatmute")) {
				e.setCancelled(true);
				String message = plugin.config.getString("Messages.chat-muted");

				if (message == null) {
					Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cMute chat message has not been defined. Please contact an admin.");
					p.sendMessage(component);
					return;
				}

				if (message.length() > 0) {
					p.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
				}
			}
		}
		if(plugin.config.getBoolean("staff-chat")) {
			Component msg = e.message();

			if (p.hasMetadata("staffchat")) {
				if (p.getMetadata("staffchat").get(0).asBoolean()) {
					e.setCancelled(true);
					Utils.staffChat(p, msg);
				}
			}
		}
		if(plugin.config.getBoolean("chat-format")) {
			if(plugin.config.getString("Messages.chat-format") == null) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cChat format message has not been defined. Please contact an admin.");
				p.sendMessage(component);
				return;
			}

			ChatComposer composer = new ChatComposer();
			e.composer(composer);
		}
		if(plugin.getConfig().getBoolean("punishment-system")) {
			try {
				PreparedStatement stmt = plugin.conn.prepareStatement("SELECT * FROM punishments WHERE type = ? AND active = ? AND user = ?");
				stmt.setString(1, "MUTE");
				stmt.setInt(2, 1);
				stmt.setString(3, e.getPlayer().getUniqueId().toString());
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					if(rs.getDate("expires").getTime() > Instant.now(Clock.systemUTC()).toEpochMilli()) {
						e.setCancelled(true);
						Component component = Component.text(
								"You are muted for "
						)
								.color(NamedTextColor.GRAY)
								.append(
										Component.text(rs.getString("reason"))
												.color(NamedTextColor.RED)
								)
								.append(
										Component.text("!" + "\nExpires at " + Utils.getRemainingTime(rs.getDate("expires").getTime()))
												.color(NamedTextColor.GRAY)
								);
						p.sendMessage(component);
					} else {
						stmt = plugin.conn.prepareStatement("UPDATE punishments SET active = ? WHERE id = ?");
						stmt.setInt(1, 0);
						stmt.setString(2, rs.getString("id"));
						stmt.executeUpdate();
					}
				}
			} catch (SQLException err) {
				err.printStackTrace();
			}
		}
	}

}
