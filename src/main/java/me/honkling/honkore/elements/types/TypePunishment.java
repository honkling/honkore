package me.honkling.honkore.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import me.honkling.honkore.lib.Punishment;
import org.jetbrains.annotations.Nullable;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

public class TypePunishment {

	static {
		Classes.registerClass(new ClassInfo<>(Punishment.class, "punishment")
				.user("punishment")
				.name("Punishment")
				.description("Represents a honkore punishment.")
				.examples("punishment with id \"A1B2\"")
				.defaultExpression(new EventValueExpression<>(Punishment.class))
				.parser(new Parser<Punishment>() {

					@Override
					@Nullable
					public Punishment parse(String input, ParseContext content) {
						return null;
					}

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(Punishment punishment, int i) {
						return toVariableNameString(punishment);
					}

					@Override
					public String toVariableNameString(Punishment punishment) {
						return punishment.type.toString() + " punishment on " + punishment.user.getName() + " with id " + punishment.id;
					}

					@Override
					public String getVariableNamePattern() {
						return "(BAN|KICK|WARN|MUTE) punishment on [A-Za-z0-9_]{3,16} with id [A-Z]{4}";
					}
				}).serializer(new Serializer<Punishment>() {

					@Override
					public Fields serialize(Punishment punishment) throws NotSerializableException {
						Fields fields = new Fields();
						fields.putObject("user", punishment.user);
						fields.putObject("moderator", punishment.moderator);
						fields.putPrimitive("type", punishment.type.toString());
						fields.putPrimitive("time", punishment.now.toEpochMilli());
						fields.putPrimitive("reason", punishment.reason.toString().trim());
						fields.putPrimitive("active", punishment.active == 1);
						fields.putPrimitive("id", punishment.id);
						return fields;
					}

					@Override
					public Punishment deserialize(Fields fields) throws StreamCorruptedException {
						return Punishment.getPunishment(fields.getPrimitive("id", String.class));
					}

					@Override
					public void deserialize(Punishment punishment, Fields fields) throws StreamCorruptedException, NotSerializableException {
						assert false;
					}

					@Override
					public boolean mustSyncDeserialization() {
						return true;
					}

					@Override
					protected boolean canBeInstantiated() {
						return false;
					}
				})
		);
	}

}
