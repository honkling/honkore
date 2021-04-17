package me.honkling.honkore.lib;

import me.honkling.honkore.Honkore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Configuration {

	private final Honkore plugin = Honkore.getInstance();
	private FileConfiguration config;
	private FileConfiguration jarConf;

	public Configuration() {
		InputStream confIS = plugin.getResource("config.yml");
		assert confIS != null;
		Reader conf = new InputStreamReader(confIS);

		jarConf = YamlConfiguration.loadConfiguration(conf);

		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		config = plugin.getConfig();
	}

	public String getString(String path) {
		String value = config.getString(path);
		if(value == null) {
			String default_value = jarConf.getString(path);
			if(default_value == null) {
				plugin.getLogger().warning("Tried to access path '" + path + "', but it is not defined in config.yml or the default jar resource file.");
				return null;
			}
			plugin.getLogger().warning("Config '" + path + "' has not been defined. The default value will be used, but you should define it in your config.yml.");
			return default_value;
		}
		return value;
	}

	public Boolean getBoolean(String path) {
		Boolean value = config.getBoolean(path);
		if(value == null) {
			Boolean default_value = jarConf.getBoolean(path);
			if(default_value == null) {
				plugin.getLogger().warning("Tried to access path '" + path + "', but it is not defined in config.yml or the default jar resource file.");
				return null;
			}
			plugin.getLogger().warning("Config '" + path + "' has not been defined. The default value will be used, but you should define it in your config.yml.");
			return default_value;
		}
		return value;
	}

}
