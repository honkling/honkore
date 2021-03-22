package me.honkling.honkore.commands.report;

import me.honkling.honkore.Honkore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ResolveCommand implements CommandExecutor {

	private Honkore plugin;

	public ResolveCommand(Honkore plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			Connection conn = this.plugin.conn;
			if(args.length < 1) return false;
			try {
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM reports WHERE id = ?");
				stmt.setString(1, args[0].toUpperCase());
				stmt.executeUpdate();
				p.sendMessage("§7Successfully resolved the report.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

}
