package club.spreadme.spider.scheduler.impl;

import club.spreadme.spider.deduplication.DeduplicationService;
import club.spreadme.spider.config.RedisConfig;
import club.spreadme.spider.scheduler.SchedulerService;
import redis.clients.jedis.Jedis;

/**
 * @author Wangshuwei
 * @since 2018-4-21
 */
public class RedisSchedulerService implements SchedulerService {

    private static final String SPIDER_KEY = "SPIDER";
    private static final String QUEUE_PREFIX = "QUEUE_";

    private DeduplicationService deduplicationService;
    private RedisConfig redisConfig;

    public RedisSchedulerService(RedisConfig redisConfig){
        this.redisConfig=redisConfig;
    }

    @Override
    public SchedulerService setDeduplicationService(DeduplicationService deduplicationService) {
        this.deduplicationService = deduplicationService;
        return this;
    }

    @Override
    public void push(String url) {
        if (deduplicationService.isNoRepeat(url)) {
            try(Jedis jedis= RedisPoolManager.getRedis(redisConfig)){
                jedis.rpush(QUEUE_PREFIX + SPIDER_KEY, url);

            }
        }
    }

    @Override
    public String poll() {
        String url;
        try(Jedis jedis= RedisPoolManager.getRedis(redisConfig)){
             url = jedis.lpop(QUEUE_PREFIX + SPIDER_KEY);
        }
        return url;
    }

}
