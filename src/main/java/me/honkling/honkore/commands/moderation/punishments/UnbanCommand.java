package me.honkling.honkore.commands.moderation.punishments;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnbanCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if(args.length < 1) return false;
		OfflinePlayer user = Bukkit.getOfflinePlayer(args[0]);
		try {
			PreparedStatement stmt = plugin.conn.prepareStatement("SELECT * FROM punishments WHERE user = ? AND active = 1");
			stmt.setString(1, user.getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				stmt = plugin.conn.prepareStatement("UPDATE punishments SET active = 0 WHERE id = ?");
				stmt.setString(1, rs.getString("id"));
				stmt.executeUpdate();
				Bukkit.getBanList(BanList.Type.NAME).pardon(user.getName());

				String message = plugin.config.getString("Messages.punishment-revoked");

				if(message == null) {
					Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cPunishment revoked message has not been defined. Please contact an admin.");
					sender.sendMessage(component);
					return true;
				}

				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
				component = Utils.translate(component, "\\{STAFF\\}", sender.getName());
				component = Utils.translate(component, "\\{PUNISHMENT\\}", "unbanned");
				component = Utils.translate(component, "\\{USER\\}", user.getName());

				Bukkit.broadcast(component, "honkore.punish");
			}
		} catch (SQLException e) {
			String message = plugin.config.getString("Messages.punishment-failed");

			if(message == null) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cPunishment error message has not been defined. Please contact an admin.");
				sender.sendMessage(component);
				e.printStackTrace();
				return true;
			}

			Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
			component = Utils.translate(component, "\\{ERROR\\}", "SQL_ERROR");
			sender.sendMessage(component);

			e.printStackTrace();
		}
		return true;
	}

}
