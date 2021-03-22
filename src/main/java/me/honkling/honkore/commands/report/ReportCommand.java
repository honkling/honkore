package me.honkling.honkore.commands.report;

import me.honkling.honkore.Honkore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

public class ReportCommand implements CommandExecutor {

	private Honkore plugin;

	public ReportCommand(Honkore plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasMetadata("report-cooldown") && (System.currentTimeMillis() / 1000L) < p.getMetadata("report-cooldown").get(0).asInt()) {
				p.sendMessage(String.format("§7You must wait %o seconds before running this command again!", p.getMetadata("report-cooldown").get(0).asInt() - (System.currentTimeMillis() / 1000L)));
				return true;
			}
			if(args.length < 2) return false;
			Plugin pl = Bukkit.getPluginManager().getPlugin("Honkore");
			assert pl != null;
			p.setMetadata("report-cooldown", new FixedMetadataValue(pl, (System.currentTimeMillis() / 1000L) + 5));
			Connection conn = plugin.conn;
			StringBuilder sb = new StringBuilder();
			for(int index = 0; index < args.length; index++) {
				if(index > 0) {
					sb.append(args[index] + " ");
				}
			}
			try {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO reports (user, reporter, reason, date, id)" +
						"VALUES(?, ?, ?, ?, ?)");
				stmt.setString(1, args[0]);
				stmt.setString(2, p.getName());
				stmt.setString(3, sb.toString().trim());
				stmt.setDate(4, new java.sql.Date(Instant.now().getEpochSecond() * 1000));
				stmt.setString(5, this.generateID());
				stmt.executeUpdate();
				Bukkit.broadcast(String.format("§3%s §7has filed a report against §3%s §7for §c%s§7.", p.getName(), args[0], sb.toString().trim()), "honkore.reports");
				p.sendMessage(String.format("§7A report against %s has been sent to all online staff.", args[0]));
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return true;
	}

	private String generateID() {
		String[] charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("");
		String result = "";
		for(int i = 0; i < 4; i++) {
			result = result + charset[(int) Math.round(Math.random()*charset.length)];
		}
		Connection conn = this.plugin.conn;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM reports WHERE id = \"%s\"", result));
			if(rs.next()) {
				return this.generateID();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

}
