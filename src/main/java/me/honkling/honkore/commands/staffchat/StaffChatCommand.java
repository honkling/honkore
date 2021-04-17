package me.honkling.honkore.commands.staffchat;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class StaffChatCommand implements CommandExecutor {

	private final Honkore plugin = Honkore.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;

			if(args.length > 0) {
				String msg = String.join(" ", args);
				Utils.staffChat(player.getPlayer(), Component.text(msg));
				return true;
			}

			boolean isEnabled = player.hasMetadata("staffchat") && player.getMetadata("staffchat").get(0).asBoolean();
			player.setMetadata("staffchat", new FixedMetadataValue(plugin, !isEnabled));

			String message = plugin.config.getString("Messages.toggle-staff-chat");

			if(message == null) {
				Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&cToggle staff chat message has not been defined. Please contact an admin.");
				sender.sendMessage(component);
				return true;
			}

			Component toggleStaffChatComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

			toggleStaffChatComponent = Utils.translate(toggleStaffChatComponent, "\\{STATE}", !isEnabled ? "enabled" : "disabled");
			player.sendMessage(toggleStaffChatComponent);
		}
		return true;
	}

}
