package me.honkling.honkore.commands.staffchat;

import me.honkling.honkore.Honkore;
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

import java.util.Collection;

public class StaffChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length > 0) {
				String msg = String.join(" ", args);
				Utils.staffChat(p.getPlayer(), msg);
			} else {
				boolean isEnabled = p.hasMetadata("staffchat") && p.getMetadata("staffchat").get(0).asBoolean();
				Plugin plugin = Bukkit.getPluginManager().getPlugin("honkore");
				assert plugin != null;
				p.setMetadata("staffchat", new FixedMetadataValue(plugin, !isEnabled));

				FileConfiguration config = plugin.getConfig();
				String message = config.getString("Messages.toggle-staff-chat");
				message = message.replaceAll("\\{STATE}", !isEnabled ? "enabled" : "disabled");
				p.sendMessage(Utils.format(message));
			}
		}
		return true;
	}

}
