package icu.ripley.spigot.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.config = plugin.getConfig();
        addConfigDefaults();
        plugin.saveDefaultConfig();
    }

    private void addConfigDefaults() {
        config.addDefault("redis.host", "CHANGEME");
        config.addDefault("redis.port", "CHANGEME");
        config.addDefault("redis.password", "CHANGEME");

        config.addDefault("server.name", "CHANGEME");
        config.addDefault("server.node", "CHANGEME");
        config.addDefault("server.ip", "CHANGEME");
        config.addDefault("server.port", "CHANGEME");
        config.addDefault("server.private", false);
    }

    public boolean checkIfConfigChanged() {
        return !config.getString("redis.host").equals("CHANGEME") && !config.getString("redis.password").equals("CHANGEME") && !config.getString("server.name").equals("CHANGEME") && !config.getString("server.node").equals("CHANGEME");
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
