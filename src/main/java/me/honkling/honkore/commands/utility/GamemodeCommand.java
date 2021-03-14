package me.honkling.honkore.commands.utility;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GamemodeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(!(sender instanceof Player) && args.length == 0) {
			Bukkit.getLogger().info("§7You can't execute a gamemode command as console without providing a player!");
			return true;
		}
		Player p = (Player) sender;
		String SETGM = "§7Successfully set §3%s§7's gamemode to §3%s§7!";
		String YOURGMSET = "§7Your gamemode has been set to §3%s!";
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
		switch (label) {
			case "gmc":
				assert target != null;
				target.setGameMode(GameMode.CREATIVE);
				p.sendMessage(String.format(SETGM, target.getName(), "CREATIVE"));
				if(target != p) {
					target.sendMessage(String.format(YOURGMSET, "CREATIVE"));
				}
				break;
				case "gms":
					assert target != null;
					target.setGameMode(GameMode.SURVIVAL);
					p.sendMessage(String.format(SETGM, target.getName(), "SURVIVAL"));
					if(target != p) {
						target.sendMessage(String.format(YOURGMSET, "SURVIVAL"));
					}
					break;
				case "gma":
					assert target != null;
					target.setGameMode(GameMode.ADVENTURE);
					p.sendMessage(String.format(SETGM, target.getName(), "ADVENTURE"));
					if(target != p) {
						target.sendMessage(String.format(YOURGMSET, "ADVENTURE"));
					}
					break;
				case "gmsp":
					assert target != null;
					target.setGameMode(GameMode.SPECTATOR);
					p.sendMessage(String.format(SETGM, target.getName(), "SPECTATOR"));
					if(target != p) {
						target.sendMessage(String.format(YOURGMSET, "SPECTATOR"));
					}
					break;
				default:
					p.sendMessage("§7You shouldn't have gotten this message, if you did, please contact §3Goose#1832 §7on Discord with the following info:\nLABEL: " + label + "\nARGS: " + String.join(", ", args));
			}

		return true;
	}

}
