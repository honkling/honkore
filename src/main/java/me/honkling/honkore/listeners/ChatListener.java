package me.honkling.honkore.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.ChatComposer;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

	private final Honkore plugin = Honkore.getInstance();

	@EventHandler
	public void onChatMessage(AsyncChatEvent e) {
		Player p = e.getPlayer();

		if(plugin.getConfig().getBoolean("chat-tools")) {
			if (plugin.chatMuted && !p.hasPermission("honkore.bypasschatmute")) {
				e.setCancelled(true);
				String message = plugin.getConfig().getString("Messages.chat-muted");

				if (message == null) {
					plugin.getLogger().warning("Mute chat message has not been defined. Skipping...");
					return;
				}

				if (message.length() > 0) {
					p.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
				}
			}
		}
		if(plugin.getConfig().getBoolean("staff-chat")) {
			Component msg = e.message();

			if (p.hasMetadata("staffchat")) {
				if (p.getMetadata("staffchat").get(0).asBoolean()) {
					e.setCancelled(true);
					Utils.staffChat(p, msg);
				}
			}
		}
		if(plugin.getConfig().getBoolean("chat-format")) {
			if(plugin.getConfig().getString("Messages.chat-format") == null) {
				plugin.getLogger().warning("Chat format message has not been defined. Skipping...");
				return;
			}

			ChatComposer composer = new ChatComposer();
			e.composer(composer);
		}
	}

}
