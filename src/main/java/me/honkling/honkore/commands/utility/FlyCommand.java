package me.honkling.honkore.commands.utility;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(!(sender instanceof Player) && args.length == 0) {
			Bukkit.getLogger().info("§7You can't execute the fly command as console without providing a player!");
			return true;
		}

		Player player = (Player) sender;
		FileConfiguration config = plugin.getConfig();

		Component setFlyComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(config.getString("Messages.set-fly"));
		Component yourFlySetComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(config.getString("Messages.your-set-fly"));
		Component notOnlineComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(Utils.getNotOnlineMessage());

		if(args.length > 0) {
			boolean online = false;
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

			if (Bukkit.getOnlinePlayers().contains(target.getPlayer()))
				online = true;

			if(!online) {
				player.sendMessage(notOnlineComponent);
				return true;
			}
		}

		Player target = Bukkit.getPlayer(args.length > 0 ? args[0] : player.getName());

		if (target.getAllowFlight()) {
			target.setAllowFlight(false);

			setFlyComponent = Utils.translate(setFlyComponent, "\\{PLAYER}", target.getName());
			setFlyComponent = Utils.translate(setFlyComponent, "\\{STATE}", "disabled");
			player.sendMessage(setFlyComponent);

			if(player != target) {
				yourFlySetComponent = Utils.translate(yourFlySetComponent, "\\{STATE}", "disabled");
				target.sendMessage(yourFlySetComponent);
			}
		} else {
			target.setAllowFlight(true);

			setFlyComponent = Utils.translate(setFlyComponent, "\\{PLAYER}", target.getName());
			setFlyComponent = Utils.translate(setFlyComponent, "\\{STATE}", "enabled");
			player.sendMessage(setFlyComponent);

			if(player != target) {
				yourFlySetComponent = Utils.translate(yourFlySetComponent, "\\{STATE}", "enabled");
				target.sendMessage(yourFlySetComponent);
			}
		}
		return true;
	}

}
