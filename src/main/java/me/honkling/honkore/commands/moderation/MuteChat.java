package me.honkling.honkore.commands.moderation;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MuteChat implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			plugin.chatMuted = !plugin.chatMuted;

			for(Player player : Bukkit.getOnlinePlayers()) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getConfig().getString("Messages.mute-chat"));

				component = Utils.translate(component, "\\{PLAYER}", sender.getName());
				component = Utils.translate(component, "\\{STATE}", plugin.chatMuted ? "muted" : "unmuted");

				player.sendMessage(component);
			}
		}

		return true;
	}

}
