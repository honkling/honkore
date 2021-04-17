package me.honkling.honkore.commands.vanish;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if(sender instanceof Player) {
			Player player = (Player) sender;
			boolean isEnabled = player.hasMetadata("vanish") && player.getMetadata("vanish").get(0).asBoolean();
			player.setMetadata("vanish", new FixedMetadataValue(plugin, !isEnabled));

			String message = plugin.config.getString("Messages.vanish");
			if (message == null) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cVanish message has not been defined. Please contact an admin.");
				sender.sendMessage(component);
				return true;
			}

			String enable = !isEnabled ? "enabled" : "disabled";

			Component vanishComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
			vanishComponent = Utils.translate(vanishComponent, "\\{STATE}", enable);
			player.sendMessage(vanishComponent);

			if(!isEnabled) {
				for(Player member : Bukkit.getOnlinePlayers()) {
					if(member != player && !member.hasPermission("honkore.vanish")) {
						member.hidePlayer(plugin, player);
					}
				}
			} else {
				for(Player member : Bukkit.getOnlinePlayers()) {
					if(member != player) {
						member.showPlayer(plugin, player);
					}
				}
			}
		}

		return true;
	}

}
