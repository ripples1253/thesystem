package icu.ripley.system.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.logging.Level;

public final class Bungee extends Plugin {

    final JedisPoolConfig poolConfig = new JedisPoolConfig();
    final JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379, 0);
    final Jedis subscriberJedis = jedisPool.getResource();
    final ServerCreationListener subscriber = new ServerCreationListener();

    @Override
    public void onEnable() {
        // Plugin startup logic
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
    }
}
