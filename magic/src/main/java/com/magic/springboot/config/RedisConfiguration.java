package com.magic.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;

/**
 * Created by ifuck on 2018/8/21.
 */
public class RedisConfiguration {
    private int database;
    private String host;
    private int port;
    private Pool pool;
    private String password;


    public static class Pool {
        public int maxIdle;
        public int minIdle;
        public int maxActive;
        public long maxWait;
    }


    public StringRedisTemplate getRedisTemplate() {
        if (StringUtils.isEmpty(host)){
            return null;
        }
        StringRedisTemplate ret = new StringRedisTemplate();
        ret.setConnectionFactory(
                connectionFactory(host, port, password, pool.maxIdle, pool.minIdle, database, pool.maxActive, pool.maxWait));
        ret.afterPropertiesSet();

        return ret;
    }

    public RedisConnectionFactory connectionFactory(String hostName, int port, String password, int maxIdle,
                                                    int minIdle, int database, int maxActive, long maxWait) {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(hostName);
        jedis.setPort(port);
        if (StringUtils.isEmpty(password) == false) {
            jedis.setPassword(password);
        }
        if (database != 0) {
            jedis.setDatabase(database);
        }
        jedis.setPoolConfig(poolCofig(maxIdle, minIdle, maxActive, maxWait));
        // 初始化连接pool
        jedis.afterPropertiesSet();
        RedisConnectionFactory factory = jedis;

        return factory;
    }

    public JedisPoolConfig poolCofig(int maxIdle, int minIdle, int maxActive, long maxWait) {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMinIdle(minIdle);
        poolCofig.setMaxWaitMillis(maxWait);
        poolCofig.setMaxTotal(maxActive);
        return poolCofig;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
