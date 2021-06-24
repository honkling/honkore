package me.honkling.honkore.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.honkling.honkore.Honkore;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EffUnban extends Effect {

	static {
		Skript.registerEffect(EffUnban.class, "[honkore] make %commandsender% unban %players%");
	}

	private final Honkore plugin = Honkore.getInstance();
	private Expression<CommandSender> moderator;
	private Expression<Player> player;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
		moderator = (Expression<CommandSender>) expressions[0];
		player = (Expression<Player>) expressions[1];
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "honkore unban effect with moderator " + moderator.toString(event, debug) + "unbanning players " + player.toString(event, debug);
	}

	@Override
	protected void execute(Event event) {
		if(moderator == null || player == null) return;
		for(Player user : player.getAll(event)) {
			try {
				PreparedStatement stmt = plugin.conn.prepareStatement("SELECT * FROM punishments WHERE user = ? AND active = 1 AND type = ?");
				stmt.setString(1, user.getUniqueId().toString());
				stmt.setString(2, "BAN");
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					stmt = plugin.conn.prepareStatement("UPDATE punishments SET active = 0 WHERE id = ?");
					stmt.setString(1, rs.getString("id"));
					stmt.executeUpdate();
					Bukkit.getBanList(BanList.Type.NAME).pardon(user.getName());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
