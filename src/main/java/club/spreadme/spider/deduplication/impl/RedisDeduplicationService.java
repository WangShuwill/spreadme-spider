package club.spreadme.spider.deduplication.impl;

import club.spreadme.spider.config.RedisConfig;
import club.spreadme.spider.deduplication.DeduplicationService;
import club.spreadme.spider.scheduler.impl.RedisPoolManager;
import redis.clients.jedis.Jedis;

/**
 * @author Wangshuwei
 * @since 2018-4-21
 */
public class RedisDeduplicationService implements DeduplicationService {

    private static final String SPIDER_KEY="SPIDER";
    private static final String SET_PREFIX="SET_";

    private RedisConfig redisConfig;

    public RedisDeduplicationService(RedisConfig redisConfig){
        this.redisConfig=redisConfig;
    }

    @Override
    public boolean isNoRepeat(String targetUrl) {
        try(Jedis jedis= RedisPoolManager.getRedis(redisConfig)){
            if(jedis.zrank(SET_PREFIX+SPIDER_KEY,targetUrl)==null){
                jedis.zadd(SET_PREFIX+SPIDER_KEY,System.currentTimeMillis(),targetUrl);
                return true;
            }
        }
        return false;
    }
}
