package me.honkling.honkore.commands.staffchat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class StaffChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length > 0) {
				String msg = String.join(" ", args);
				Collection<? extends Player> staff = Bukkit.getOnlinePlayers();
				for(Player member : staff) {
					if(member.hasPermission("honkore.staffchat")) {
						member.sendMessage(String.format("§d[STAFF] §7%s: %s", member.getName(), msg));
					}
				}
			} else {
				boolean isEnabled = p.hasMetadata("staffchat") && p.getMetadata("staffchat").get(0).asBoolean();
				Plugin plugin = Bukkit.getPluginManager().getPlugin("honkore");
				assert plugin != null;
				p.setMetadata("staffchat", new FixedMetadataValue(plugin, !isEnabled));
				p.sendMessage(String.format("§7Staff chat has been %s.", !isEnabled ? "enabled" : "disabled"));
			}
		}
		return true;
	}

}
