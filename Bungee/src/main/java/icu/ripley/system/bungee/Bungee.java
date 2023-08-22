package icu.ripley.system.bungee;

import icu.ripley.system.shared.DatabaseUtilities;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public final class Bungee extends Plugin {
    JedisPool jedisPool;

    private String redisHost;
    private int redisPort;
    private String redisPassword;

    @Override
    public void onEnable() {
        // jedis stuff
        try {
            initConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jedisPool = DatabaseUtilities.createJedisPool(redisHost, redisPort, redisPassword);

        final Jedis subscriberJedis = jedisPool.getResource();
        final ServerCreationListener subscriber = new ServerCreationListener();

        new Thread(() -> {
            try {
                this.getLogger().info("!! ATTEMPTING SUBSCRIBE TO SERVER EVENTS. IF FAIL, REBOOT !!");
                subscriberJedis.subscribe(subscriber, "ServerEvents");
                this.getLogger().info("!! SUBSCRIBE ENDED. THAT AIN'T NORMAL UNLESS YOU'RE REBOOTING !!");
            } catch (Exception e) {
                this.getLogger().log(Level.SEVERE, "!! SHIT FAILED !!" + e);
            }
        }).start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        jedisPool.destroy();
    }

    public void initConfig() throws IOException {
        // Create plugin config folder if it doesn't exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), "config.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            getLogger().info("!! ENTER REDIS DETAILS IN CONFIG, THEN START BUNGEE !!");
            getProxy().stop("TheSystem configuration required.");
        }

        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));

        if(configuration.getString("redis.host").equals("CHANGEME")
                || configuration.getInt("redis.port") == 0000
                || configuration.getString("redis.password").equals("CHANGEME")) {
            getLogger().info("!! ENTER REDIS DETAILS IN CONFIG, THEN START BUNGEE !!");
            getProxy().stop("TheSystem configuration required.");
        }

        redisHost = configuration.getString("redis.host");
        redisPort = configuration.getInt("redis.port");
        redisPassword = configuration.getString("redis.password");
    }
}
