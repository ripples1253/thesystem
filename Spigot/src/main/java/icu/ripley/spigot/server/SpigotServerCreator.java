package icu.ripley.spigot.server;

import icu.ripley.system.shared.SpigotServer;
import org.bukkit.configuration.file.FileConfiguration;

public class SpigotServerCreator {
    public SpigotServer createSpigotServerDetails(FileConfiguration config) {
        return new SpigotServer(config.getString("server.name"), config.getString("server.node"), config.getString("server.ip"), config.getInt("server.port"), config.getBoolean("server.private"));
    }
}
