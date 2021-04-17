package me.honkling.honkore.commands.moderation.punishments;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PunsCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();
	private Player p;
	private OfflinePlayer user;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if(args.length <= 0) return false;
		if(sender instanceof Player) {
			p = (Player) sender;
			user = Bukkit.getOfflinePlayer(args[0]);
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
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM punishments WHERE user = ?");
			stmt.setString(1, user.getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			Menu gui = new Menu(plugin, InventoryType.CHEST, 6, "Viewing all punishments of " + user.getName());
			setupPage(gui);
			List<String> rows = new ArrayList<>();
			while(rs.next()) rows.add(rs.getString("user"));
			rs = stmt.executeQuery();
			rs.next();
			int listSize = rows.size();
			if(listSize <= 0) return gui;
			for(int i = 0; i < 54 && i <= listSize; i++) {
				OfflinePlayer mod = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("moderator")));
				ItemStack punishmentItem = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta punishmentMeta = (SkullMeta) punishmentItem.getItemMeta();
				punishmentMeta.setOwningPlayer(mod);
				punishmentMeta.setDisplayName("§3" + mod.getName());
				List<String> lore = new ArrayList<>();
				lore.add("§7Moderator: §3" + mod.getName());
				lore.add("§7Reason: §3" + rs.getString("reason"));
				lore.add("§7Date: §3" + rs.getDate("date").toString());
				lore.add("§7Expires at: §3" + rs.getDate("expires").toString());
				lore.add("§7Type: §3" + rs.getString("type"));
				lore.add("§7ID: §3" + rs.getString("id"));
				lore.add("§7Active?: §3" + (rs.getInt("active") == 1 ? "Yes" : "No"));
				punishmentMeta.setLore(lore);
				punishmentItem.setItemMeta(punishmentMeta);
				gui.getSlot(gui.getFirstEmptySlot()).setItem(punishmentItem);
				if(!rs.next()) break;
			}
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
