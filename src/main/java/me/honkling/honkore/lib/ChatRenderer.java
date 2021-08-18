package me.honkling.honkore.lib;

import me.honkling.honkore.Honkore;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatRenderer implements io.papermc.paper.chat.ChatRenderer {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public @NotNull Component render(@NotNull Player player, @NotNull Component display, @NotNull Component message, @NotNull Audience audience) {
		String format = plugin.config.getString("Messages.chat-format");

		if(format == null) {
			Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cChat format message has not been defined. Please contact an admin.");
			player.sendMessage(component);
			return component;
		}

		Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(format);
		component = Utils.translate(component, "\\{DISPLAYNAME\\}", LegacyComponentSerializer.legacyAmpersand().serialize(display));
		component = Utils.translate(component, "\\{MESSAGE\\}", LegacyComponentSerializer.legacyAmpersand().serialize(message));

		return component;
	}
}
