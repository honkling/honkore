package me.honkling.honkore.listeners;

import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;

public class LoginListener implements Listener {

	private final Honkore plugin = Honkore.getInstance();

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		try {
			PreparedStatement stmt = plugin.conn.prepareStatement("SELECT * FROM punishments WHERE user = ? AND active = ? AND type = ?");
			stmt.setString(1, e.getPlayer().getUniqueId().toString());
			stmt.setInt(2, 1);
			stmt.setString(3, "BAN");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				if(rs.getDate("expires").getTime() > Instant.now(Clock.systemUTC()).toEpochMilli()) {
					Component component = Component.text(
							"You have been banned!\nExpires at " + Utils.getRemainingTime(rs.getDate("expires").getTime())
					)
							.color(NamedTextColor.GRAY)
							.append(
									Component.text("\n \n" + rs.getString("reason"))
											.color(NamedTextColor.RED)
							);
					e.kickMessage(component);
				} else {
					stmt = plugin.conn.prepareStatement("UPDATE punishments SET active = ? WHERE id = ?");
					stmt.setInt(1, 0);
					stmt.setString(2, rs.getString("id"));
					stmt.executeUpdate();
				}
			}
		} catch (SQLException err) {
			err.printStackTrace();
		}
	}

}
