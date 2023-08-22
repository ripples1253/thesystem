package icu.ripley.spigot.redis;

import icu.ripley.system.shared.DatabaseUtilities;
import icu.ripley.system.shared.EventMessageUtils;
import icu.ripley.system.shared.EventTypes.ServerEventType;
import icu.ripley.system.shared.SpigotServer;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Slf4j
public class RedisPublisher {
    private final JedisPool jedisPool;

    public RedisPublisher(String host, int port, String password) {
        this.jedisPool = DatabaseUtilities.createJedisPool(host, port, password);
    }

    public void publishEventMessageToRedis(ServerEventType type, SpigotServer spigotServer) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish("ServerEvents", EventMessageUtils.to(ServerEventType.CREATE, spigotServer));
        } catch (Exception e) {
            log.error("Error in publishEventMessageToRedis method", e);
        }
    }

    public void destroyJedisPool() {
        jedisPool.destroy();
    }
}
