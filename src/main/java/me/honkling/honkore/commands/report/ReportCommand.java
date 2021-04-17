package me.honkling.honkore.commands.report;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
import java.sql.SQLException;
import java.time.Instant;

public class ReportCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

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
			Player user = Bukkit.getPlayer(args[0]);
			if(!user.isOnline()) {
				String message = plugin.getConfig().getString("Messages.not-online");

				if(message == null) {
					plugin.getLogger().warning("Not online message has not been defined. Skipping...");
					return true;
				}

				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
				component = Utils.translate(component, "\\{PLAYER\\}", user.getName());

				p.sendMessage(component);
			}
			try {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO reports (user, reporter, reason, date, id)" +
						"VALUES(?, ?, ?, ?, ?)");
				stmt.setString(1, user.getUniqueId().toString());
				stmt.setString(2, p.getUniqueId().toString());
				stmt.setString(3, sb.toString().trim());
				stmt.setDate(4, new java.sql.Date(Instant.now().getEpochSecond() * 1000));
				stmt.setString(5, Utils.generateID());
				stmt.executeUpdate();

				String staffReportMSG = plugin.config.getString("Messages.user-is-reported");

				if(staffReportMSG == null) {
					Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cStaff user report message has not been defined. Please contact an admin.");
					sender.sendMessage(component);
					return true;
				}

				Component staffReport = LegacyComponentSerializer.legacyAmpersand().deserialize(staffReportMSG);
				staffReport = Utils.translate(staffReport, "\\{AUTHOR\\}", p.getName());
				staffReport = Utils.translate(staffReport, "\\{USER\\}", user.getName());
				staffReport = Utils.translate(staffReport, "\\{REASON\\}", sb.toString().trim());

				String userReportMSG = plugin.config.getString("Messages.reported-user-successfully");

				if(userReportMSG == null) {
					Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cUser reported message has not been defined. Please contact an admin.");
					sender.sendMessage(component);
					return true;
				}

				Component userReport = LegacyComponentSerializer.legacyAmpersand().deserialize(userReportMSG);
				userReport = Utils.translate(userReport, "\\{PLAYER\\}", user.getName());

				Bukkit.broadcast(staffReport, "honkore.reports");
				p.sendMessage(userReport);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return true;
	}

}
