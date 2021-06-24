package me.honkling.honkore.lib;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.honkling.honkore.Honkore;
import me.honkling.honkore.elements.expressions.ExprPunishment;
import me.honkling.honkore.lib.Punishment;
import org.bukkit.event.Event;

public class ExprPunishmentProperty extends SimpleExpression<Object> {

	static {
		Skript.registerExpression(ExprPunishment.class, Punishment.class, ExpressionType.COMBINED,"[honkore] [property] <.*> of [punishment] %punishment%");
	}

	private final Honkore plugin = Honkore.getInstance();
	private String property;
	private Expression<Punishment> punishment;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		property = parseResult.regexes.get(0).group();
		punishment = (Expression<Punishment>) expressions[0];
		return true;
	}

	@Override
	public Class<? extends Object> getReturnType() {
		return Object.class;
	}

	@Override
	protected Object[] get(Event event) {
		Punishment pun = punishment.getSingle(event);
		switch (property) {
			case "user":
				return new Object[]{ pun.user };
			case "moderator":
				return new Object[]{ pun.moderator };
			case "type":
				return new Object[]{ pun.type.toString() };
			case "time":
				return new Object[]{ pun.now.toEpochMilli() };
			case "reason":
				return new Object[]{ pun.reason.toString().trim() };
			case "active":
				return new Object[]{ pun.active };
			case "id":
				return new Object[]{ pun.id };
			default:
				Skript.error("Invalid honkore punishment property: " + property);
				return null;
			}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public String toString(Event event, boolean debug) {
		Punishment pun = punishment.getSingle(event);
		return "honkore punishment property expression with " + pun.type.toString() + " punishment on player " + pun.user.getName() + " with id " + pun.id;
	}
}
