package icu.ripley.system.shared;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class DatabaseUtilities {

    public static JedisPool createJedisPool(String redisHost, int redisPort, String redisPassword){
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        return new JedisPool(poolConfig, redisHost, redisPort, 0, redisPassword);
    }

}
