package me.honkling.honkore.commands.report;

import me.honkling.honkore.Honkore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResolveCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			Connection conn = plugin.conn;
			if(args.length < 1) return false;
			try {
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM reports WHERE id = ?");
				stmt.setString(1, args[0].toUpperCase());
				stmt.executeUpdate();

				String message = plugin.config.getString("Messages.report-resolved");

				if(message == null) {
					Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cReport resolved message has not been defined. Please contact an admin.");
					sender.sendMessage(component);
					return true;
				}

				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

				p.sendMessage(component);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

}
