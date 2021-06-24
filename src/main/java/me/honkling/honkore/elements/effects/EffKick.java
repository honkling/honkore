package me.honkling.honkore.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.honkling.honkore.lib.Punishment;
import me.honkling.honkore.lib.PunishmentType;
import me.honkling.honkore.lib.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffKick extends Effect {

	static {
		Skript.registerEffect(EffKick.class, "[honkore] make %commandsender% kick %players% [(for|because|with reason|due to) %-strings%]");
	}

	private Expression<CommandSender> moderator;
	private Expression<Player> player;
	private Expression<String> reason;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
		moderator = (Expression<CommandSender>) expressions[0];
		player = (Expression<Player>) expressions[1];
		reason = (Expression<String>) expressions[2];
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "honkore kick effect with moderator " + moderator.toString(event, debug) + "kicking players " + player.toString(event, debug) + " and reason " + reason.toString(event, debug);
	}

	@Override
	protected void execute(Event event) {
		if(moderator == null || player == null) return;
		for(Player user : player.getAll(event)) {
			if(reason != null) {
				StringBuilder fullReason = Utils.concat(reason.getAll(event));
				Punishment punishment = new Punishment(
						user,
						moderator.getSingle(event),
						PunishmentType.KICK,
						"10de",
						fullReason
				);
				punishment.execute();
			}
		}
	}

}
