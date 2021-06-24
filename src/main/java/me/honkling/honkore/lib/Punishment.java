package me.honkling.honkore.lib;

import me.honkling.honkore.Honkore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public class Punishment {

	private final Honkore plugin = Honkore.getInstance();
	public OfflinePlayer user;
	public CommandSender moderator;
	public PunishmentType type;
	public String time;
	public StringBuilder reason;
	public String id;
	public Instant now;
	public int active;

	public Punishment(@NotNull OfflinePlayer user, @NotNull CommandSender moderator, @NotNull PunishmentType type, String time, @NotNull StringBuilder reason) {
		this.user = user;
		this.moderator = moderator;
		this.type = type;
		this.time = time;
		this.reason = reason;
	}

	public static Punishment getPunishment(String id) {
		try {
			PreparedStatement stmt = Honkore.getInstance().conn.prepareStatement("SELECT * FROM punishments WHERE id = ?");
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Punishment punishment = new Punishment(
						Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("user"))),
						!rs.getString("moderator").equals("CONSOLE") ? (CommandSender) Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("moderator"))) : Bukkit.getConsoleSender(),
						PunishmentType.valueOf(rs.getString("type")),
						"10de",
						Utils.concat(rs.getString("reason").split(" "))
				);
				punishment.id = rs.getString("id");
				punishment.active = rs.getInt("active");
				punishment.now = Instant.ofEpochMilli(rs.getDate("date").getTime());
				return punishment;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*stmt.executeUpdate("CREATE TABLE IF NOT EXISTS punishments(" +
					"user TEXT NOT NULL," +
					"moderator TEXT NOT NULL," +
					"reason TEXT NOT NULL," +
					"type TEXT NOT NULL," +
					"date DATE NOT NULL," +
					"expires DATE," +
					"id TEXT NOT NULL" +
					"active INT" +
					")");*/

	public void execute() {
		this.now = Instant.now(Clock.systemUTC());
		long expiration = now.toEpochMilli() + (Utils.convertToInt(time) * 1000L);
		String id = Utils.generateID();
		Connection conn = plugin.conn;
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE punishments SET active = 0 WHERE active = 1 AND user = ? AND type = ?");
			stmt.setString(1, user.getUniqueId().toString());
			stmt.setString(2, type.toString());
			if(type == PunishmentType.BAN || type == PunishmentType.MUTE) {
				stmt = conn.prepareStatement("INSERT INTO punishments(user, moderator, reason, type, date, expires, id, active)" +
						"VALUES(?, ?, ?, ?, ?, ?, ?, 1)");
				stmt.setString(7, id);
				stmt.setDate(6, new Date(expiration));
			} else if(type == PunishmentType.KICK || type == PunishmentType.WARN) {
				stmt = conn.prepareStatement("INSERT INTO punishments(user, moderator, reason, type, date, id)" +
						"VALUES(?, ?, ?, ?, ?, ?)");
				stmt.setString(6, id);
			} else {
				plugin.getLogger().warning("Unknown punishment type encountered. Please make an issue on the GitHub.");
				return;
			}
			stmt.setString(1, user.getUniqueId().toString());
			stmt.setString(2, moderator instanceof Player ? ((Player) this.moderator).getUniqueId().toString() : "CONSOLE");
			stmt.setString(3, reason.toString().trim());
			stmt.setString(4, type.toString());
			stmt.setDate(5, new Date(now.toEpochMilli()));
			stmt.executeUpdate();
			this.id = id;
			this.active = 1;
			if(user.isOnline()) {
				Player p = (Player) user;
				if (type == PunishmentType.BAN || type == PunishmentType.KICK) {
					Component component = Component.text(
							type == PunishmentType.BAN ? "You have been banned!\nExpires at " + Utils.getRemainingTime(expiration) : "You have been kicked!"
					)
							.color(NamedTextColor.GRAY)
							.append(
									Component.text("\n \n" + reason)
											.color(NamedTextColor.RED)
							);
					p.kick(component);
					if(type == PunishmentType.BAN) {
						p.banPlayer(reason.toString().trim(), Date.from(Instant.ofEpochMilli(expiration)), moderator.getName(), true);
					}
				} else if (type == PunishmentType.MUTE || type == PunishmentType.WARN) {
					Component component = Component.text(
							type == PunishmentType.MUTE ? "You are muted for " : "You have been warned for "
					)
							.color(NamedTextColor.GRAY)
							.append(
									Component.text(reason.toString().trim())
									.color(NamedTextColor.RED)
							)
							.append(
									Component.text("!" + (type == PunishmentType.MUTE ? "\nExpires at " + Utils.getRemainingTime(expiration) : ""))
									.color(NamedTextColor.GRAY)
							);
					p.sendMessage(component);
				}
			}
			String message = plugin.config.getString("Messages.punishment-issued");

			if(message == null) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cPunishment issued message has not been defined. Please contact an admin.");
				moderator.sendMessage(component);
				return;
			}

			Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
			component = Utils.translate(component, "\\{STAFF\\}", moderator.getName());
			component = Utils.translate(component, "\\{PUNISHMENT\\}", type == PunishmentType.BAN ? "banned" : (type == PunishmentType.KICK ? "kicked" : (type == PunishmentType.MUTE ? "muted" : "warned")));
			component = Utils.translate(component, "\\{USER\\}", user.getName());
			component = Utils.translate(component, "\\{REASON\\}", reason.toString().trim());
			component = Utils.translate(component, "\\{LENGTH\\}", Utils.convertToStr(time));
			moderator.sendMessage(component);
		} catch (SQLException e) {
			String message = plugin.config.getString("Messages.punishment-failed");

			if(message == null) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cPunishment error message has not been defined. Please contact an admin.");
				moderator.sendMessage(component);
				e.printStackTrace();
				return;
			}

			Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
			component = Utils.translate(component, "\\{ERROR\\}", "SQL_ERROR");
			moderator.sendMessage(component);

			e.printStackTrace();
		}
	}

}
