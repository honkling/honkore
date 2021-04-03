package me.honkling.honkore.listeners;

import me.honkling.honkore.lib.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
	ISSUE: UNUSED CLASS
 */
public class StaffChatListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChatMessage(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();

		if (p.hasMetadata("staffchat")) {
			if (p.getMetadata("staffchat").get(0).asBoolean()) {
				e.setCancelled(true);
				Utils.staffChat(p, msg);
			}
		}
	}

}
