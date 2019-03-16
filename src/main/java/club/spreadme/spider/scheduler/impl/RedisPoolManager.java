package club.spreadme.spider.scheduler.impl;

import club.spreadme.spider.config.RedisConfig;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Wangshuwei
 * @since 2018-4-21
 */
public class RedisPoolManager {

    private static JedisPool pool;
    
    public static Jedis getRedis(RedisConfig redisConfig){
        JedisPoolConfig poolConfig=redisConfig.getPoolConfig();
        if(StringUtils.isNotBlank(redisConfig.getPasswd())){
            pool =new JedisPool(poolConfig,redisConfig.getIp(),redisConfig.getPort(),
                    redisConfig.getTimeout(),redisConfig.getPasswd(),redisConfig.getDatabase());
        }else{
            pool =new JedisPool(poolConfig,redisConfig.getIp(),redisConfig.getPort(),redisConfig.getTimeout());
        }
        return pool.getResource();
    }

}
