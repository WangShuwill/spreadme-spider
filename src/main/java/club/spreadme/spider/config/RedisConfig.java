package club.spreadme.spider.config;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Wangshuwei
 * @since 2018-4-26
 */
public class RedisConfig {

    private String ip;
    private int port=6379;
    private int timeout=10*1000;
    private int database=0;
    private String passwd;
    private JedisPoolConfig poolConfig;

    public String getIp() {
        return ip;
    }

    public RedisConfig setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RedisConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public RedisConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int getDatabase() {
        return database;
    }

    public RedisConfig setDatabase(int database) {
        this.database = database;
        return this;
    }

    public String getPasswd() {
        return passwd;
    }

    public RedisConfig setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public RedisConfig setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        return this;
    }
}
