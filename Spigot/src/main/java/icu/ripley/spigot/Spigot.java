package icu.ripley.spigot;

import icu.ripley.system.shared.DatabaseUtilities;
import icu.ripley.system.shared.EventMessageUtils;
import icu.ripley.system.shared.EventTypes.ServerEventType;
import icu.ripley.system.shared.SpigotServer;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.logging.Level;

public final class Spigot extends JavaPlugin {

    @Getter
    private final FileConfiguration config = this.getConfig();

    private JedisPool jedisPool;

    private SpigotServer spigotServer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.addConfigDefaults();
        this.saveDefaultConfig();
        if(!checkIfConfigChanged()){
            for(int i = 0; i < 25; i++){
                getLogger().log(Level.SEVERE, "!!!! YOU NEED TO CHANGE THE CONFIG BEFORE BOOTING !!!!");
            }
            getServer().shutdown();
        }

        spigotServer = createSpigotServerDetails();

        jedisPool = DatabaseUtilities.createJedisPool(config.getString("redis.host"),
                config.getInt("redis.port"),
                config.getString("redis.password"));

        Jedis jedisTest = jedisPool.getResource();

        jedisTest.publish("ServerEvents", EventMessageUtils.to(ServerEventType.CREATE, spigotServer));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void addConfigDefaults(){
        config.addDefault("redis.host", "CHANGEME");
        config.addDefault("redis.port", "CHANGEME");
        config.addDefault("redis.password", "CHANGEME");

        config.addDefault("server.name", "CHANGEME");
        config.addDefault("server.node", "CHANGEME");
        config.addDefault("server.ip", "CHANGEME");
        config.addDefault("server.port", "CHANGEME");
        config.addDefault("server.private", false);
    }

    private boolean checkIfConfigChanged(){
        return !config.getString("redis.host").equals("CHANGEME")
                && !config.getString("redis.password").equals("CHANGEME")
                && !config.getString("server.name").equals("CHANGEME")
                && !config.getString("server.node").equals("CHANGEME");
    }

    private SpigotServer createSpigotServerDetails(){
        return new SpigotServer(config.getString("server.name"),
                config.getString("server.node"),
                config.getString("server.ip"),
                config.getInt("server.port"),
                config.getBoolean("server.private"));
    }
}
