package me.honkling.honkore;

import me.honkling.honkore.commands.moderation.ClearChat;
import me.honkling.honkore.commands.moderation.MuteChat;
import me.honkling.honkore.commands.report.ReportCommand;
import me.honkling.honkore.commands.report.ReportsCommand;
import me.honkling.honkore.commands.report.ResolveCommand;
import me.honkling.honkore.commands.staffchat.StaffChatCommand;
import me.honkling.honkore.commands.vanish.VanishCommand;
import me.honkling.honkore.listeners.ChatMuteListener;
import me.honkling.honkore.listeners.VanishJoinListener;
import me.honkling.honkore.listeners.VanishQuitListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.honkling.honkore.commands.utility.GamemodeCommand;
import me.honkling.honkore.commands.utility.FlyCommand;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public final class Honkore extends JavaPlugin {

	public boolean chatMuted = false;
	public Connection conn = null;

	@Override
	public void onEnable() {
		getLogger().info("honkore has enabled :)");
		saveDefaultConfig();
		FileConfiguration config = getConfig();
		if(config.getBoolean("chat-tools")) {
			Objects.requireNonNull(getCommand("mutechat")).setExecutor(new MuteChat(this));
			Objects.requireNonNull(getCommand("clearchat")).setExecutor(new ClearChat());
		}
		if(config.getBoolean("utility-commands")) {
			Objects.requireNonNull(getCommand("gmc")).setExecutor(new GamemodeCommand());
			Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand());
		}
		if(config.getBoolean("staff-chat")) {
			Objects.requireNonNull(getCommand("sc")).setExecutor(new StaffChatCommand());
			getServer().getPluginManager().registerEvents(new ChatMuteListener(this), this);
		}
		if(config.getBoolean("report-system")) {
			Objects.requireNonNull(getCommand("report")).setExecutor(new ReportCommand(this));
			Objects.requireNonNull(getCommand("reports")).setExecutor(new ReportsCommand(this));
			Objects.requireNonNull(getCommand("resolve")).setExecutor(new ResolveCommand(this));
		}
		if(config.getBoolean("vanish-system")) {
			Objects.requireNonNull(getCommand("vanish")).setExecutor(new VanishCommand());
			getServer().getPluginManager().registerEvents(new VanishJoinListener(), this);
			getServer().getPluginManager().registerEvents(new VanishQuitListener(), this);
		}
		String dataFolder = getDataFolder().getAbsolutePath() + (getDataFolder().getAbsolutePath().endsWith("/") || getDataFolder().getAbsolutePath().endsWith("\\") ? "" : "/");
		String dbUrl = String.format("jdbc:sqlite:%sinfo.db", dataFolder);
		try {
			conn = DriverManager.getConnection(dbUrl);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS reports(" +
					"user TEXT NOT NULL," +
					"reporter TEXT NOT NULL," +
					"reason TEXT NOT NULL," +
					"date DATE NOT NULL," +
					"id TEXT NOT NULL" +
					")");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS punishments(" +
					"user TEXT NOT NULL," +
					"moderator TEXT NOT NULL," +
					"reason TEXT NOT NULL," +
					"type TEXT NOT NULL," +
					"date DATE NOT NULL," +
					"expires DATE," +
					"id TEXT NOT NULL" +
					")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("darn. honkore has disabled.");
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		getLogger().info("Bye! :)");
	}

}