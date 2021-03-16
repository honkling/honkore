package me.honkling.honkore.commands.vanish;

import me.honkling.honkore.lib.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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

			FileConfiguration config = plugin.getConfig();

			p.setMetadata("vanish", new FixedMetadataValue(plugin, !isEnabled));

			String message = config.getString("Messages.vanish");
			if (message == null) {
				plugin.getLogger().warning("Vanish message has not been defined. Skipping...");
				return true;
			}

			String enable = !isEnabled ? "enabled" : "disabled";

			message = message.replaceAll("\\{STATE}", enable);

			p.sendMessage(Utils.format(message));

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
