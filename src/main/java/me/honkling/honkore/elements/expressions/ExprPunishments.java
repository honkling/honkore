package me.honkling.honkore.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.honkling.honkore.Honkore;
import me.honkling.honkore.lib.Punishment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExprPunishments extends SimpleExpression<Punishment> {

	static {
		Skript.registerExpression(ExprPunishments.class, Punishment.class, ExpressionType.COMBINED,"[all] [honkore] punishments of %player%");
	}

	private final Honkore plugin = Honkore.getInstance();
	private Expression<Player> player;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		player = (Expression<Player>) expressions[0];
		return true;
	}

	@Override
	public Class<? extends Punishment> getReturnType() {
		return Punishment.class;
	}

	@Override
	protected Punishment[] get(Event event) {
		try {
			PreparedStatement stmt = plugin.conn.prepareStatement("SELECT * FROM punishments WHERE user = ?");
			stmt.setString(1, player.getSingle(event).getUniqueId().toString());
			ResultSet rs = stmt.executeQuery();
			List<Punishment> punishments = new ArrayList<>();
			while(rs.next()) {
				punishments.add(Punishment.getPunishment(rs.getString("id")));
			}
			return punishments.toArray(new Punishment[]{});
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public String toString(Event event, boolean debug) {
		return "honkore punishments expression with player " + player.toString(event, debug);
	}
}
