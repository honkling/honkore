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

public class EffBan extends Effect {

	static {
		Skript.registerEffect(EffBan.class, "[honkore] make %commandsender% ban %players% [(for|because|with reason|due to) %-strings%] [(for|with) [length] <^(([0-9]{1,}(s|m|h|d|w|mo|y|de)){1,}|forever)$>]");
	}

	private Expression<CommandSender> moderator;
	private Expression<Player> player;
	private Expression<String> reason;
	private String length;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
		moderator = (Expression<CommandSender>) expressions[0];
		player = (Expression<Player>) expressions[1];
		reason = (Expression<String>) expressions[2];
		length = parser.regexes.get(0).group();
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "honkore ban effect with moderator " + moderator.toString(event, debug) + "banning players " + player.toString(event, debug) + " and reason " + reason.toString(event, debug) + " and length " + length;
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
						PunishmentType.BAN,
						length.equals("forever") ? "10de" : length,
						fullReason
				);
				punishment.execute();
			}
		}
	}

}
