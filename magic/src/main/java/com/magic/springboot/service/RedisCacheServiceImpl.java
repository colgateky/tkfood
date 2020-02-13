package com.magic.springboot.service;

import com.magic.springboot.client.Client;
import com.magic.springboot.config.MultiConfig;
import com.magic.utils.JsonUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by xm on 2017/5/20.
 */
@Service
public class RedisCacheServiceImpl implements RedisCacheService {
    @Resource
    protected StringRedisTemplate stringRedisTemplate;
    protected boolean useGlobal;
    @Resource
    protected Client client;

    protected StringRedisTemplate get() {
        if (MultiConfig.enableMulti && !useGlobal && MultiConfig.MultiListener != null) {
            return MultiConfig.MultiListener.getRedisTemplate(client.getClientId());
        }
        return stringRedisTemplate;
    }

    public void hashSet(String key, String hashKey, Object value) {
        String s = JsonUtils.toJson(value);
        get().opsForHash().put(key, hashKey, s);
    }
    public boolean hashSetIf(String key, String hashKey, Object value) {
        String s = JsonUtils.toJson(value);
        return get().opsForHash().putIfAbsent(key, hashKey, s);
    }
    public long hashCount(String key) {
        return get().opsForHash().size(key);
    }
    public <T> Map<String, T> hashGetAll(String key, Class<T> cls) {
        Map<String, T> ret = new HashMap<>();
        Map<Object, Object> m = get().opsForHash().entries(key);
        for (Map.Entry<Object, Object> e : m.entrySet()) {
            T v = JsonUtils.parseJson(e.getValue().toString(), cls);
            ret.put(e.getKey().toString(), v);
        }
        return ret;
    }

    @Override
    public Set<String> hashGetAllKeys(String key) {
        Set<Object> keys = get().opsForHash().keys(key);
        Set<String> ret = new HashSet<>();
        if (keys != null) {
            for (Object k : keys) {
                ret.add(k.toString());
            }
        }
        return ret;
    }

    public <T> T hashGet(String key, String hashKey, Class<T> cls) {
        String s = (String)get().opsForHash().get(key, hashKey);
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        T ret = JsonUtils.parseJson(s, cls);

        return ret;
    }
    public void hashDel(String key, String hashKey) {
        get().opsForHash().delete(key, hashKey);
    }

    @Override
    public void hashDelAll(String key) {
        get().delete(key);
    }

    @Override
    public void lpush(String key, Object value) {
        get().opsForList().leftPush(key, JsonUtils.toJson(value));
    }

    @Override
    public <T> T rpop(String key, Class<T> cls) {
        String ret = get().opsForList().rightPop(key);
        if (StringUtils.isEmpty(ret)) {
            return null;
        }
        return JsonUtils.parseJson(ret, cls);
    }

    public Long getTime() {
        Long ret = null;
        try {
            ret = get().execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    try {
                        Long time = connection.time();
                        return time;
                    } finally {
                        connection.close();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public void setTmp(String key, String value, long timeoutSecs) {
        get().opsForValue().set(key, value, timeoutSecs, TimeUnit.SECONDS);
    }

    @Override
    public String getTmp(String key) {
        String ret = get().opsForValue().get(key);
        return ret;
    }

    @Override
    public void setTmpObject(String key, Object value, long timeoutSecs) {
        setTmp(key, JsonUtils.toJson(value), timeoutSecs);
    }

    @Override
    public <T> T getTmpObject(String key, Class<T> cls) {
        String ret = getTmp(key);
        if (StringUtils.isEmpty(ret)) {
            return null;
        }
        return JsonUtils.parseJson(ret, cls);
    }

    @Override
    public Set<String> getAllKeys(String pattern) {
        return get().keys(pattern);
    }


    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean isUseGlobal() {
        return useGlobal;
    }

    public void setUseGlobal(boolean useGlobal) {
        this.useGlobal = useGlobal;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
