package me.honkling.honkore.lib;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class Slot {

	Menu parent;
	ItemStack item;
	BiConsumer<Slot, Player> listener = null;
	int index;

	public Slot(Menu parent, ItemStack item, int index) {
		this.parent = parent;
		this.item = item;
		this.index = index;
	}

	public ItemStack getItem() {
		return this.item;
	}

	public void setItem(ItemStack item) {
		this.parent.inventory.setItem(this.index, item);
		this.parent.slots.set(this.index, this);
	}

	public int getIndex() {
		return this.index;
	}

	public void setItem(ItemStack item, BiConsumer<Slot, Player> fn) {
		this.parent.inventory.setItem(this.index, item);
		this.listener = fn;
		this.parent.slots.set(this.index, this);
	}
}
