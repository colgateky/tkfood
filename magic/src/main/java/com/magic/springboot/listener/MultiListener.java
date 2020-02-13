package com.magic.springboot.listener;

import com.magic.springboot.client.Client;
import com.magic.springboot.config.MongoConfiguration;
import com.magic.springboot.config.RedisConfiguration;
import com.magic.springboot.dao.DaoHelper;
import com.magic.springboot.exception.ApiErrorCode;
import com.magic.springboot.service.MagicService;
import com.magic.springboot.service.MagicServiceImpl;
import com.magic.springboot.service.RedisCacheServiceImpl;
import com.magic.springboot.service.RedisLockServiceImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ifuck on 2018/8/21.
 */
public abstract class MultiListener {
    public static class MongoConfig {
        public String host;
        public int port;
        public String database;
        public String username;
        public String password;
    }
    public static class RedisConfig {
        public String host;
        public int port;
        public int database;
        public String password;
    }

    protected MagicServiceImpl globalMagicService;
    protected RedisCacheServiceImpl globalRedisCacheService;
    protected RedisLockServiceImpl globalRedisLockService;

    public final void init(DaoHelper daoHelper1, RedisCacheServiceImpl redisCacheService1, RedisLockServiceImpl redisLockService1) {
        if (globalRedisCacheService == null) {
            DaoHelper daoHelper = new DaoHelper();
            daoHelper.setMongoTemplate(daoHelper1.getMongoTemplate());
            daoHelper.setUseGlobal(true);
            MagicServiceImpl magicService = new MagicServiceImpl();
            magicService.setDaoHelper(daoHelper);
            globalMagicService = magicService;

            RedisCacheServiceImpl redisCacheService = new RedisCacheServiceImpl();
            redisCacheService.setUseGlobal(true);
            redisCacheService.setStringRedisTemplate(redisCacheService1.getStringRedisTemplate());
            globalRedisCacheService = redisCacheService;

            RedisLockServiceImpl redisLockService = new RedisLockServiceImpl();
            redisLockService.setUseGlobal(true);
            redisLockService.setStringRedisTemplate(redisLockService1.getStringRedisTemplate());
            globalRedisLockService =redisLockService;
        }
    }
    protected abstract MongoConfig getMongoConfig(String siteId);
    protected abstract RedisConfig getRedisConfig(String siteId);
    protected abstract String getGameServerConfig(String clientId);
    protected abstract String getGameClientConfig(String clientId);


    private Map<String, MongoTemplate> mongoTemplateMap = new HashMap<>();
    private Map<String, StringRedisTemplate> redisTemplateMap = new HashMap<>();
    private Map<String, String> gameServerMap = new HashMap<>();
    private Map<String, String> gameClientMap = new HashMap<>();

    public synchronized MongoTemplate getMongoTemplate(String clientId) {
        MongoTemplate ret = mongoTemplateMap.get(clientId);
        if (ret != null) {
            return ret;
        }
        MongoConfig config = getMongoConfig(clientId);
        if (config == null) {
            ApiErrorCode.throwError("数据库初始化失败");
        }

        MongoConfiguration mc = new MongoConfiguration();
        mc.setHost(config.host);
        mc.setPort(config.port);
        mc.setDatabase(config.database);
        mc.setUsername(config.username);
        mc.setPassword(config.password);
        try {
            ret = mc.createMongoTemplate();
            mongoTemplateMap.put(clientId, ret);
        } catch (Exception exp) {
            exp.printStackTrace();
            ApiErrorCode.throwError(exp.getMessage());
        }
        return ret;
    }

    public synchronized StringRedisTemplate getRedisTemplate(String clientId) {
        StringRedisTemplate ret = redisTemplateMap.get(clientId);
        if (ret != null) {
            return ret;
        }
        RedisConfig config = getRedisConfig(clientId);
        if (config == null) {
            ApiErrorCode.throwError("内存数据库初始化失败");
        }

        RedisConfiguration configuration = new RedisConfiguration();
        configuration.setHost(config.host);
        configuration.setPort(config.port);
        configuration.setDatabase(config.database);
        configuration.setPassword(config.password);

        RedisConfiguration.Pool pool = new RedisConfiguration.Pool();
        pool.maxIdle = 8;
        pool.minIdle = 0;
        pool.maxActive = 8;
        pool.maxWait = -1;
        configuration.setPool(pool);
        try {
            ret = configuration.getRedisTemplate();
            redisTemplateMap.put(clientId, ret);
        } catch (Exception exp) {
            exp.printStackTrace();
            ApiErrorCode.throwError(exp.getMessage());
        }

        return ret;
    }

    public synchronized String getGameServer(String clientId){
        String ret = gameServerMap.get(clientId);
        if (!StringUtils.isEmpty(ret)){
            return ret;
        }
        try {
            ret = getGameServerConfig(clientId);
            gameServerMap.put(clientId, ret);
        } catch (Exception exp) {
            exp.printStackTrace();
            ApiErrorCode.throwError(exp.getMessage());
        }

        return ret;
    }

    public synchronized String getGameClientId(String clientId){
        String ret = gameClientMap.get(clientId);
        if (!StringUtils.isEmpty(ret)){
            return ret;
        }
        try {
            ret = getGameClientConfig(clientId);
            gameClientMap.put(clientId, ret);
        } catch (Exception exp) {
            exp.printStackTrace();
            ApiErrorCode.throwError(exp.getMessage());
        }

        return ret;
    }
}
