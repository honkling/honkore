package me.honkling.honkore.commands.moderation.punishments;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Punishment;
import me.honkling.honkore.lib.PunishmentType;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class WarnCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if(args.length < 2) return false;
		StringBuilder reason = Utils.concat(Arrays.copyOfRange(args, 1, args.length));
		String length = Utils.convertToStr("10de");
		Punishment punishment = new Punishment(
				Bukkit.getOfflinePlayer(args[0]),
				sender,
				PunishmentType.WARN,
				"10de",
				reason
		);
		if(length.contains("UNKNOWN")) {
			String message = plugin.config.getString("Messages.punishment-failed");

			if(message == null) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cPunishment failure message has not been defined. Please contact an admin.");
				sender.sendMessage(component);
				return true;
			}

			Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
			component = Utils.translate(component, "\\{ERROR\\}", "INVALID_TIMESPAN_PROVIDED");

			sender.sendMessage(component);

			return true;
		}
		punishment.execute();
		return true;
	}

}
