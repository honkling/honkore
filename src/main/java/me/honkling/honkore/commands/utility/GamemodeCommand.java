package me.honkling.honkore.commands.utility;

import me.honkling.honkore.lib.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GamemodeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(!(sender instanceof Player) && args.length == 0) {
			Bukkit.getLogger().info("§7You can't execute a gamemode command as console without providing a player!");
			return true;
		}
		Player p = (Player) sender;

		Plugin plugin = Bukkit.getPluginManager().getPlugin("honkore");
		assert plugin != null;
		FileConfiguration config = plugin.getConfig();

		String SETGM = config.getString("Messages.set-gamemode"); //"§7Successfully set §3%s§7's gamemode to §3%s§7!";
		String YOURGMSET = config.getString("Messages.your-set-gamemode"); //"§7Your gamemode has been set to §3%s!";

		String NOTONLINE = Utils.getNotOnlineMessage();
		Player target = Bukkit.getPlayer(args.length > 0 ? args[0] : p.getName());
		if(target == null) {

			p.sendMessage(Utils.format(NOTONLINE));
			return true;

		}

		switch (label) {
			case "gmc":
				assert target != null;
				target.setGameMode(GameMode.CREATIVE);
				SETGM = SETGM.replaceAll("\\{PLAYER}", target.getName());
				SETGM = SETGM.replaceAll("\\{GAMEMODE}", "CREATIVE");
				p.sendMessage(Utils.format(SETGM));
				if(target != p) {
					YOURGMSET = YOURGMSET.replaceAll("\\{GAMEMODE}", "CREATIVE");
					target.sendMessage(Utils.format(YOURGMSET));
				}
				break;
			case "gms":
				assert target != null;
				target.setGameMode(GameMode.SURVIVAL);
				SETGM = SETGM.replaceAll("\\{PLAYER}", target.getName());
				SETGM = SETGM.replaceAll("\\{GAMEMODE}", "SURVIVAL");
				p.sendMessage(Utils.format(SETGM));
				if(target != p) {
					YOURGMSET = YOURGMSET.replaceAll("\\{GAMEMODE}", "SURVIVAL");
					target.sendMessage(Utils.format(YOURGMSET));
				}
				break;
			case "gma":
				assert target != null;
				target.setGameMode(GameMode.ADVENTURE);
				SETGM = SETGM.replaceAll("\\{PLAYER}", target.getName());
				SETGM = SETGM.replaceAll("\\{GAMEMODE}", "ADVENTURE");
				p.sendMessage(Utils.format(SETGM));
				if(target != p) {
					YOURGMSET = YOURGMSET.replaceAll("\\{GAMEMODE}", "ADVENTURE");
					target.sendMessage(Utils.format(YOURGMSET));
				}
				break;
			case "gmsp":
				assert target != null;
				target.setGameMode(GameMode.SPECTATOR);
				SETGM = SETGM.replaceAll("\\{PLAYER}", target.getName());
				SETGM = SETGM.replaceAll("\\{GAMEMODE}", "SPECTATOR");
				p.sendMessage(Utils.format(SETGM));
				if(target != p) {
					YOURGMSET = YOURGMSET.replaceAll("\\{GAMEMODE}", "SPECTATOR");
					target.sendMessage(Utils.format(YOURGMSET));
				}
				break;
			default:
				return false;//p.sendMessage("§7You shouldn't have gotten this message, if you did, please contact §3Goose#1832 §7on Discord with the following info:\nLABEL: " + label + "\nARGS: " + String.join(", ", args));
			}

		return true;
	}

}
