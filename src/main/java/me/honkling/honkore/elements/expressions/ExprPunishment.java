package me.honkling.honkore.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.honkling.honkore.lib.Punishment;
import org.bukkit.event.Event;

public class ExprPunishment extends SimpleExpression<Punishment> {

	static {
		Skript.registerExpression(ExprPunishment.class, Punishment.class, ExpressionType.COMBINED,"[honkore] punishment (with|from) [id] %string%");
	}

	private Expression<String> id;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		id = (Expression<String>) expressions[0];
		return true;
	}

	@Override
	public Class<? extends Punishment> getReturnType() {
		return Punishment.class;
	}

	@Override
	protected Punishment[] get(Event event) {
		Punishment punishment = Punishment.getPunishment(id.getSingle(event));
		assert punishment != null;
		return new Punishment[]{ punishment };
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public String toString(Event event, boolean debug) {
		return "honkore punishment expression with id " + id.toString(event, debug);
	}
}
