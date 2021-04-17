package me.honkling.honkore.commands;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Configuration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HonkoreCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if(args.length <= 0) return false;
		switch (args[0]) {
			case "reload":
				plugin.config = new Configuration();

				String message = plugin.config.getString("Messages.honkore-reload");

				if(message == null) {
					Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cHonkore reload message has not been defined. Please contact an admin.");
					sender.sendMessage(component);
					return true;
				}

				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
				sender.sendMessage(component);
				break;
		}
		// more will be added later
		return true;
	}

}
