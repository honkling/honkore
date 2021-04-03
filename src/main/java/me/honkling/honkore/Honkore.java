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

import java.io.File;
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
			getCommand("mutechat").setExecutor(new MuteChat());
			getCommand("clearchat").setExecutor(new ClearChat());
		}
		if(config.getBoolean("utility-commands")) {
			getCommand("gmc").setExecutor(new GamemodeCommand());
			getCommand("fly").setExecutor(new FlyCommand());
		}
		if(config.getBoolean("staff-chat")) {
			getCommand("sc").setExecutor(new StaffChatCommand());
			getServer().getPluginManager().registerEvents(new ChatMuteListener(), this);
		}
		if(config.getBoolean("report-system")) {
			getCommand("report").setExecutor(new ReportCommand(this));
			getCommand("reports").setExecutor(new ReportsCommand(this));
			getCommand("resolve").setExecutor(new ResolveCommand(this));
		}
		if(config.getBoolean("vanish-system")) {
			getCommand("vanish").setExecutor(new VanishCommand());
			getServer().getPluginManager().registerEvents(new VanishJoinListener(), this);
			getServer().getPluginManager().registerEvents(new VanishQuitListener(), this);
		}
		String dbUrl = String.format("jdbc:sqlite:%sinfo.db", getDataFolder() + File.separator);
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

	/**
	 * Gets an instance of the main Honkore class
	 * @return Honkore
	 */
	public static Honkore getInstance() {
		return getPlugin(Honkore.class);
	}

}