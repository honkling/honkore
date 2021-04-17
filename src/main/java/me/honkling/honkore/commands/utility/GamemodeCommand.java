package me.honkling.honkore.commands.utility;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Configuration;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GamemodeCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if (!(sender instanceof Player) && args.length == 0) {
			Bukkit.getLogger().info("§7You can't execute a gamemode command as console without providing a player!");
			return true;
		}

		Player player = (Player) sender;
		Configuration config = plugin.config;

		Component setGamemodeComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(config.getString("Messages.set-gamemode"));
		Component yourSetGamemodeComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(config.getString("Messages.your-set-gamemode"));
		Component notOnlineComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(config.getString("Messages.not-online"));

		Player target = Bukkit.getPlayer(args.length > 0 ? args[0] : player.getName());

		if(target == null) {
			player.sendMessage(notOnlineComponent);
			return true;
		}

		switch (label) {
			case "gmc":
				target.setGameMode(GameMode.CREATIVE);
				setGamemodeComponent = Utils.translate(setGamemodeComponent, "\\{PLAYER}", target.getName());
				setGamemodeComponent = Utils.translate(setGamemodeComponent, "\\{GAMEMODE}", "CREATIVE");
				player.sendMessage(setGamemodeComponent);

				if(target != player) {
					yourSetGamemodeComponent = Utils.translate(yourSetGamemodeComponent, "\\{GAMEMODE}", "CREATIVE");
					target.sendMessage(yourSetGamemodeComponent);
				}
				break;
			case "gms":
				target.setGameMode(GameMode.SURVIVAL);
				setGamemodeComponent = Utils.translate(setGamemodeComponent, "\\{PLAYER}", target.getName());
				setGamemodeComponent = Utils.translate(setGamemodeComponent, "\\{GAMEMODE}", "SURVIVAL");
				player.sendMessage(setGamemodeComponent);

				if(target != player) {
					yourSetGamemodeComponent = Utils.translate(yourSetGamemodeComponent, "\\{GAMEMODE}", "SURVIVAL");
					target.sendMessage(yourSetGamemodeComponent);
				}
				break;
			case "gma":
				target.setGameMode(GameMode.ADVENTURE);
				setGamemodeComponent = Utils.translate(setGamemodeComponent, "\\{PLAYER}", target.getName());
				setGamemodeComponent = Utils.translate(setGamemodeComponent, "\\{GAMEMODE}", "ADVENTURE");
				player.sendMessage(setGamemodeComponent);

				if(target != player) {
					yourSetGamemodeComponent = Utils.translate(yourSetGamemodeComponent, "\\{GAMEMODE}", "ADVENTURE");
					target.sendMessage(yourSetGamemodeComponent);
				}
				break;
			case "gmsp":
				target.setGameMode(GameMode.SPECTATOR);
				setGamemodeComponent = Utils.translate(setGamemodeComponent, "\\{PLAYER}", target.getName());
				setGamemodeComponent = Utils.translate(setGamemodeComponent, "\\{GAMEMODE}", "SPECTATOR");
				player.sendMessage(setGamemodeComponent);

				if(target != player) {
					yourSetGamemodeComponent = Utils.translate(yourSetGamemodeComponent, "\\{GAMEMODE}", "SPECTATOR");
					target.sendMessage(yourSetGamemodeComponent);
				}
				break;
			default:
				return false;//p.sendMessage("§7You shouldn't have gotten this message, if you did, please contact §3Goose#1832 §7on Discord with the following info:\nLABEL: " + label + "\nARGS: " + String.join(", ", args));
			}

		return true;
	}

}
