package me.honkling.honkore.commands.vanish;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			boolean isEnabled = p.hasMetadata("vanish") && p.getMetadata("vanish").get(0).asBoolean();
			Plugin plugin = Bukkit.getPluginManager().getPlugin("honkore");
			assert plugin != null;
			p.setMetadata("vanish", new FixedMetadataValue(plugin, !isEnabled));
			p.sendMessage(String.format("§7Vanish has been %s.", !isEnabled ? "enabled" : "disabled"));
			if(!isEnabled) {
				for(Player member : Bukkit.getOnlinePlayers()) {
					if(member != p && !member.hasPermission("honkore.vanish")) {
						member.hidePlayer(plugin, p);
					}
				}
			} else {
				for(Player member : Bukkit.getOnlinePlayers()) {
					if(member != p) {
						member.showPlayer(plugin, p);
					}
				}
			}
		}

		return true;
	}

}
