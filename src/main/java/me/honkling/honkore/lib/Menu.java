package me.honkling.honkore.lib;

import me.honkling.honkore.Honkore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu implements Listener {

	private Honkore plugin;

	Inventory inventory;
	String title;
	int rows;
	ArrayList<Slot> slots;

	@SuppressWarnings("deprecation")
	public Menu(Honkore plugin, InventoryType type, int rows, String title) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
		if(type == InventoryType.CHEST && (rows > 6 || rows < 1))
			throw new IllegalArgumentException("Number of rows cannot be under 1 or over 6");
		this.inventory = type == InventoryType.CHEST ?
				Bukkit.createInventory(null, rows * 9, title)
				: Bukkit.createInventory(null, type, title);
		this.title = title;
		this.rows = rows;
		this.slots = new ArrayList<>(rows * 9);
		for(int i = 0; i < this.rows * 9; i++) {
			slots.add(new Slot(this, new ItemStack(Material.AIR), i));
		}
	}

	public Slot getSlot(int index) {
		return slots.get(index);
	}

	public String getTitle() {
		return this.title;
	}

	public int getRows() {
		return this.rows;
	}

	public HashMap<Integer, Slot> getAllItems() {
		HashMap<Integer, Slot> items = new HashMap<>();
		ItemStack[] list = this.inventory.getContents();
		for(int index = 0; index < list.length; index++) {
			items.put(index, this.getSlot(index));
			index++;
		}
		return items;
	}

	public int getFirstEmptySlot() {
		return this.inventory.firstEmpty();
	}

	public void open(Player p) {
		p.openInventory(this.inventory);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		if(e.getInventory() != this.inventory) return;
		e.setCancelled(true);
		int index = e.getSlot();
		Slot slot = this.getSlot(index);
		if(slot.listener != null) {
			slot.listener.accept(slot, Bukkit.getPlayer(e.getWhoClicked().getName()));
		}
	}

	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent e) {
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryCloseEvent.getHandlerList().unregister(this);
	}

}