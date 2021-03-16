package me.honkling.honkore.commands.moderation;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
				FileConfiguration config = plugin.getConfig();
				String message = config.getString("Messages.mute-chat");
				message = message.replaceAll("\\{PLAYER}", ((Player) sender).getPlayer().getName());
				message = message.replaceAll("\\{STATE}", plugin.chatMuted ? "muted" : "unmuted");

				value.sendMessage(Utils.format(message));
			}
		}

		return true;
	}

}
