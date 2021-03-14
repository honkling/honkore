package me.honkling.honkore.commands.utility;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(!(sender instanceof Player) && args.length == 0) {
			Bukkit.getLogger().info("§7You can't execute the fly command as console without providing a player!");
			return true;
		}
		Player p = (Player) sender;
		String SETFLY = "§7Successfully %s §3%s§7's flight mode!";
		String YOURFLYSET = "§7Your flight mode has been %s!";
		String NOTONLINE = "§3%s §7is not online!";
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
			p.sendMessage(String.format(SETFLY, "disabled", target.getName()));
			if(p != target) {
				target.sendMessage(String.format(YOURFLYSET, "disabled"));
			}
		} else {
			target.setAllowFlight(true);
			p.sendMessage(String.format(SETFLY, "enabled", target.getName()));
			if(p != target) {
				target.sendMessage(String.format(YOURFLYSET, "enabled"));
			}
		}
		return true;
	}

}
