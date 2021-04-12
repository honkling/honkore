package me.honkling.honkore.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
	ISSUE: UNUSED CLASS
 */
public class StaffChatListener implements Listener {

	@EventHandler
	public void onChatMessage(AsyncChatEvent e) {
		Player p = e.getPlayer();
		Component msg = e.message();

		if (p.hasMetadata("staffchat")) {
			if (p.getMetadata("staffchat").get(0).asBoolean()) {
				e.setCancelled(true);
				Utils.staffChat(p, msg);
			}
		}
	}

}
