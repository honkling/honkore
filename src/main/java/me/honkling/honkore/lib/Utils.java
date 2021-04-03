package me.honkling.honkore.lib;

import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Utils {

    public static String getNotOnlineMessage() {
        return "§3%s §7is not online!";
    }

    public static void staffChat(Player player, String mesage) {

        for(Player member : Bukkit.getOnlinePlayers()) {
            if (!member.hasPermission("honkore.staffchat"))
                return;

            Plugin plugin = Bukkit.getPluginManager().getPlugin("honkore");
            assert plugin != null;
            FileConfiguration config = plugin.getConfig();

            String message = config.getString("Messages.staff-chat");
            if (message == null) {
                plugin.getLogger().warning("Staff chat message has not been defined. Skipping...");
                return;
            }
            message = message.replaceAll("\\{PLAYER}", player.getName());
            message = message.replaceAll("\\{MESSAGE}", mesage);

            member.sendMessage(format(message));
        }

    }

    public static String format(String toform) {
        return ChatColor.translateAlternateColorCodes('&', toform);
    }

    public static void clearChat(Player clearer) {
        for(Player value : Bukkit.getOnlinePlayers()) {

            Plugin plugin = Bukkit.getPluginManager().getPlugin("honkore");
            assert plugin != null;
            FileConfiguration config = plugin.getConfig();

            String message = config.getString("Messages.clear-chat");
            message = message.replaceAll("\\{PLAYER}", clearer.getName());
            value.sendMessage(Strings.repeat(" \n", 250) + format(message));
        }
    }


}
