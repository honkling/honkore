package me.honkling.honkore.commands.utility;

import me.honkling.honkore.lib.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(!(sender instanceof Player) && args.length == 0) {
			Bukkit.getLogger().info("§7You can't execute the fly command as console without providing a player!");
			return true;
		}
		Player p = (Player) sender;

		Plugin plugin = Bukkit.getPluginManager().getPlugin("honkore");
		assert plugin != null;
		FileConfiguration config = plugin.getConfig();

		String SETFLY = config.getString("Messages.set-fly"); //"§7Successfully %s §3%s§7's flight mode!";

		String YOURFLYSET = config.getString("Messages.your-set-fly"); //"§7Your flight mode has been %s!";
		String NOTONLINE =  Utils.getNotOnlineMessage();
		if(args.length > 0) {
			boolean online = false;
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getName().toLowerCase().equals(args[0])) {
					online = true;
				}
			}
			if(!online) {
				p.sendMessage(String.format(NOTONLINE, args[0]));
				return true;
			}
		}
		Player target = Bukkit.getPlayer(args.length > 0 ? args[0] : p.getName());
		if(target.getAllowFlight()) {
			target.setAllowFlight(false);
			SETFLY = SETFLY.replaceAll("\\{PLAYER}", target.getName());
			SETFLY = SETFLY.replaceAll("\\{STATE}", "disabled");
			p.sendMessage(Utils.format(SETFLY));
			if(p != target) {
				YOURFLYSET = YOURFLYSET.replaceAll("\\{STATE}", "disabled");
				target.sendMessage(Utils.format(YOURFLYSET));
			}
		} else {
			target.setAllowFlight(true);
			SETFLY = SETFLY.replaceAll("\\{PLAYER}", target.getName());
			SETFLY = SETFLY.replaceAll("\\{STATE}", "enabled");
			p.sendMessage(Utils.format(SETFLY));
			if(p != target) {
				YOURFLYSET = YOURFLYSET.replaceAll("\\{STATE}", "disabled");
				target.sendMessage(Utils.format(YOURFLYSET));
			}
		}
		return true;
	}

}
