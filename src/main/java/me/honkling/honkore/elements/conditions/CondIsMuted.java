package me.honkling.honkore.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.honkling.honkore.Honkore;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CondIsMuted extends Condition {

	static {
		Skript.registerCondition(CondIsMuted.class, "%player% (1¦is|2¦is(n't| not)) muted");
	}

	private final Honkore plugin = Honkore.getInstance();
	private Expression<Player> player;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
		this.player = (Expression<Player>) expressions[0];
		setNegated(parser.mark == 1);
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "honkore player is muted expression with player " + player.toString(event, debug);
	}

	@Override
	public boolean check(Event event) {
		Player p = player.getSingle(event);
		if (p == null) return isNegated();
		try {
			PreparedStatement stmt = plugin.conn.prepareStatement("SELECT * FROM punishments WHERE user = ? AND type = ? AND active = 1");
			stmt.setString(1, p.getUniqueId().toString());
			stmt.setString(2, "MUTE");
			ResultSet rs = stmt.executeQuery();
			return rs.next() == isNegated();
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

}