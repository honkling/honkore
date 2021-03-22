package me.honkling.honkore.commands.report;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReportsCommand implements CommandExecutor {

	private Honkore plugin;

	public ReportsCommand(Honkore plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			Connection conn = plugin.conn;
			Menu gui = createPage(conn);
			assert gui != null;
			Objects.requireNonNull(gui).open(p);
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	private Menu createPage(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM reports");
			Menu gui = new Menu(this.plugin, InventoryType.CHEST, 6, "Viewing all reports");
			setupPage(gui);
			List<String> rows = new ArrayList<>();
			while(rs.next()) rows.add(rs.getString("user"));
			rs = stmt.executeQuery("SELECT * FROM reports");
			rs.next();
			int listSize = rows.size();
			if(listSize <= 0) return gui;
			for(int i = 0; i < 54 && i <= listSize; i++) {
				ItemStack reportItem = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta reportMeta = (SkullMeta) reportItem.getItemMeta();
				reportMeta.setOwningPlayer(Bukkit.getOfflinePlayer(rs.getString("user")));
				reportMeta.setDisplayName("§3" + rs.getString("user"));
				List<String> lore = new ArrayList<>();
				lore.add("§7User reported: §3" + rs.getString("user"));
				lore.add("§7Report author: §3" + rs.getString("reporter"));
				lore.add("§7Reason: §3" + rs.getString("reason"));
				lore.add("§7Date: §3" + rs.getDate("date").toString());
				lore.add("§7ID: §3" + rs.getString("id"));
				lore.add(" ");
				lore.add(String.format("§7Run §3/resolve %s §7to resolve the report.", rs.getString("id")));
				reportMeta.setLore(lore);
				reportItem.setItemMeta(reportMeta);
				gui.getSlot(gui.getFirstEmptySlot()).setItem(reportItem);
				if(!rs.next()) break;
			}
			// it's a bandage solution for sure, ill probably look into the issue more later
			//gui.getSlot(gui.getFirstEmptySlot() - 1).setItem(new ItemStack(Material.AIR));
			return gui;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	private void setupPage(Menu gui) {
		ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.setDisplayName(" ");
		pane.setItemMeta(paneMeta);
		int lastSlot = (gui.getRows() * 9) - 1;
		for (int i = 0; i <= 9; i++) {
			gui.getSlot(i).setItem(pane);
		}
		for (int i = 2; i <= (gui.getRows() - 1); i++) {
			int curr = 9 * i;
			gui.getSlot(curr).setItem(pane);
			gui.getSlot(curr - 1).setItem(pane);
		}
		for (int i = lastSlot - 8; i <= lastSlot; i++) {
			gui.getSlot(i).setItem(pane);
		}
	}
}
