package me.honkling.honkore.commands.moderation;

import me.honkling.honkore.Honkore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MuteChat implements CommandExecutor {

	private Honkore plugin;

	public MuteChat(Honkore plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			plugin.chatMuted = !plugin.chatMuted;
			for(Player value : Bukkit.getOnlinePlayers()) {
				value.sendMessage(String.format("§7Chat has been %s.", plugin.chatMuted ? "muted" : "unmuted"));
			}
		}

		return true;
	}

}
