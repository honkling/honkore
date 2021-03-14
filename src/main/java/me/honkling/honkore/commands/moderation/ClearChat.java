package me.honkling.honkore.commands.moderation;

import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClearChat implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			for(Player value : Bukkit.getOnlinePlayers()) {
				value.sendMessage(Strings.repeat(" \n", 250) + "§7Chat has been cleared.");
			}
		}
		return true;
	}

}
