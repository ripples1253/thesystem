package icu.ripley.spigot;

import icu.ripley.spigot.config.ConfigManager;
import icu.ripley.spigot.redis.RedisPublisher;
import icu.ripley.spigot.server.SpigotServerCreator;
import icu.ripley.system.shared.EventTypes.ServerEventType;
import icu.ripley.system.shared.SpigotServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Spigot extends JavaPlugin {
    private ConfigManager configManager;
    private SpigotServer spigotServer;
    private RedisPublisher redisPublisher;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        if (!configManager.checkIfConfigChanged()) {
            for (int i = 0; i < 25; i++) {
                getLogger().log(Level.SEVERE, "!!!! YOU NEED TO CHANGE THE CONFIG BEFORE BOOTING !!!!");
            }
            getServer().shutdown();
        }

        spigotServer = new SpigotServerCreator().createSpigotServerDetails(configManager.getConfig());

        redisPublisher = new RedisPublisher(configManager.getConfig().getString("redis.host"),
                configManager.getConfig().getInt("redis.port"),
                configManager.getConfig().getString("redis.password"));

        redisPublisher.publishEventMessageToRedis(ServerEventType.CREATE, spigotServer);
    }

    @Override
    public void onDisable() {
        redisPublisher.publishEventMessageToRedis(ServerEventType.DESTROY, spigotServer);
        redisPublisher.destroyJedisPool();
    }


}
