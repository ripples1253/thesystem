package icu.ripley.spigot;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Spigot extends JavaPlugin {

    @Getter
    private final FileConfiguration config = this.getConfig();

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
}
