package me.honkling.honkore.lib;

import com.google.common.base.Strings;
import me.honkling.honkore.Honkore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.RegExp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class Utils {

    private static final Honkore plugin = Honkore.getInstance();

    public static void staffChat(Player player, Component message) {

        String formattedMessage = plugin.config.getString("Messages.staff-chat");

        if (formattedMessage == null) {
            Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cStaff chat message has not been defined. Please contact an admin.");
            player.sendMessage(component);
            return;
        }

        Component componentMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(formattedMessage);
        componentMessage = translate(componentMessage, "\\{PLAYER}", player.getName());
        componentMessage = translate(componentMessage, "\\{MESSAGE}", message);

        Bukkit.broadcast(componentMessage, "honkore.staffchat");

    }

    public static void clearChat(Player clearer) {
        for(Player player : Bukkit.getOnlinePlayers()) {

            String message = plugin.config.getString("Messages.clear-chat");

            if(message == null) {
                Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cClear chat message has not been defined. Please contact an admin.");
                clearer.sendMessage(component);
                return;
            }

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

    public static int convertToInt(String time) {
        int length = 0;
        String letters = time.replaceAll("[0-9]", "");
        int number = Integer.parseInt(time.replaceAll("[^0-9]", ""));
        switch (letters) {
            case "s":
                length += number;
                break;
            case "m":
                length += number * 60;
                break;
            case "h":
                length += number * 3600;
                break;
            case "d":
                length += number * (3600 * 24);
                break;
            case "w":
                length += number * (3600 * 24 * 7);
                break;
            case "mo":
                length += number * (3600 * 24 * 7 * 4);
                break;
            case "y":
                length += number * (3600 * 24 * 7 * 4 * 12);
                break;
            case "de":
                length += number * (3600 * 24 * 7 * 4 * 12 * 10);
                break;
            default:
                length -= 1;
                break;
        }
        return length;
    }

    public static String convertToStr(String time) {
        StringBuilder length = new StringBuilder();
        String letters = time.replaceAll("[0-9]", "");
        int number = Integer.parseInt(time.replaceAll("[^0-9]", ""));
        if(length.toString().equals("")) {
            length.append(String.format("%d ", number));
        } else {
            length.append(String.format(", %d ", number));
        }
        switch (letters) {
            case "s":
                length.append("SECOND").append(number == 1 ? "" : "S");
                break;
            case "m":
                length.append("MINUTE").append(number == 1 ? "" : "S");
                break;
            case "h":
                length.append("HOUR").append(number == 1 ? "" : "S");
                break;
            case "d":
                length.append("DAY").append(number == 1 ? "" : "S");
                break;
            case "w":
                length.append("WEEK").append(number == 1 ? "" : "S");
                break;
            case "mo":
                length.append("MONTH").append(number == 1 ? "" : "S");
                break;
            case "y":
                length.append("YEAR").append(number == 1 ? "" : "S");
                break;
            case "de":
                length.append("DECADE").append(number == 1 ? "" : "S");
                break;
            default:
                length.append("UNKNOWN").append(number == 1 ? "" : "S");
                break;
        }
        return length.toString();
    }

    public static StringBuilder concat(String[] args) {
        StringBuilder sb = new StringBuilder();
        for(String arg : args) {
            sb.append(arg + " ");
        }
        return sb;
    }

    public static String getRemainingTime(long millis) {
        java.util.Date date = java.util.Date.from(Instant.ofEpochMilli(millis));
        SimpleDateFormat formatter = new SimpleDateFormat("M/d/y HH:mm:ss");
        return formatter.format(date);
    }

    public static String generateID() {
        String[] charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("");
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < 4; i++) {
            result.append(charset[(int) Math.round(Math.random() * charset.length)]);
        }
        Connection conn = plugin.conn;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM punishments WHERE id = ?");
            stmt.setString(1, result.toString());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return Utils.generateID();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return result.toString();
        }
        return result.toString();
    }

}
