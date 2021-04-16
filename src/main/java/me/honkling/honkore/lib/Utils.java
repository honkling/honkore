package me.honkling.honkore.lib;

import com.google.common.base.Strings;
import me.honkling.honkore.Honkore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.RegExp;

public class Utils {

    private static final Honkore plugin = Honkore.getInstance();

    public static String getNotOnlineMessage() {
        return "§3%s §7is not online!";
    }

    public static void staffChat(Player player, Component message) {

        for(Player member : Bukkit.getOnlinePlayers()) {
            if (!member.hasPermission("honkore.staffchat"))
                return;

            String formattedMessage = plugin.getConfig().getString("Messages.staff-chat");
            if (formattedMessage == null) {
                plugin.getLogger().warning("Staff chat message has not been defined. Skipping...");
                return;
            }

            Component componentMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(formattedMessage);
            componentMessage = translate(componentMessage, "\\{PLAYER}", player.getName());
            componentMessage = translate(componentMessage, "\\{MESSAGE}", message);

            member.sendMessage(componentMessage);
        }

    }

    public static void clearChat(Player clearer) {
        for(Player player : Bukkit.getOnlinePlayers()) {

            String message = plugin.getConfig().getString("Messages.clear-chat");

            assert message != null;
            Component component = Component.text(Strings.repeat(" \n", 250)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
            component = translate(component, "\\{PLAYER}", clearer.getName());

            player.sendMessage(component);
        }
    }

    /**
     * Replaces a string within a message
     * @param message Component
     * @param key @RegExp String
     * @param replacement String
     * @return Component
     */
    public static Component translate(Component message, @RegExp String key, String replacement) {
        return message.replaceText(builder -> builder.match(key).replacement(replacement));
    }

    /**
     * Replaces a string within a message
     * @param message Component
     * @param key @RegExp String
     * @param replacement Component
     * @return Component
     */
    public static Component translate(Component message, @RegExp String key, Component replacement) {
        return message.replaceText(builder -> builder.match(key).replacement(replacement));
    }

}
