package com.magic.springboot.service;

import com.magic.springboot.client.Client;
import com.magic.springboot.config.MultiConfig;
import com.magic.springboot.exception.ApiException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by tt on 2017/3/24.
 */
@Service
public class RedisLockServiceImpl implements RedisLockService {

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

    @Override
    public boolean lockNoError(String key, LockFlow flow) {
        return this.lockInternal(key, 30000, flow);
    }

    @Override
    public void lock(String key, LockFlow flow) {
         lock(key, 30000, flow);
    }

    protected boolean lockInternal(String key, long timeoutMsecs, LockFlow flow) {
        long now = System.currentTimeMillis();
        while (true) {
            ApiException apiException = null;
            long expires = lock(key, timeoutMsecs);
            if (expires > 0) {
                try {
                    flow.run();
                    return true;
                } catch (ApiException e) {
                    apiException = e;
                } catch (Exception exp) {
                    exp.printStackTrace();
                    apiException = new ApiException(-1, "服务端异常");
                } finally {
                    if (System.currentTimeMillis() < expires) {
                        unlock(key);
                    }
                }
                if (apiException != null) {
                    throw apiException;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - now > timeoutMsecs) {
                return false;
            }
        }
    }
    @Override
    public void lock(String key, long timeoutMsecs, LockFlow flow) {
        boolean lockFlag = lockInternal(key, timeoutMsecs, flow);
        if (!lockFlag) {
            new ApiException(9999, "请求过于频繁，请稍候再试").throwSelf();
        }
    }

    private long lock(String lockKey, long timeoutMsecs) {
        long expires = System.currentTimeMillis() + timeoutMsecs + 1;
        String expiresStr = String.valueOf(expires);
        if (this.setNX(lockKey, expiresStr)) {
            return expires;
        }

        String currentValueStr = this.get(lockKey);
        if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
            String oldValueStr = this.getSet(lockKey, expiresStr);
            if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                return expires;
            }
        }
        return 0;
    }

    private void unlock(String lockKey) {
        try {
            get().execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    Long count = connection.del(serializer.serialize(lockKey));
                    connection.close();
                    return count;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果key不存在，则设置值value,并返回成功，否则失败
     * @param key
     * @param value
     * @return
     */
    private boolean setNX(final String key, final String value) {
        Object obj = null;
        try {
            obj = get().execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    Boolean success = connection.setNX(serializer.serialize(key), serializer.serialize(value ));
                    connection.close();
                    return success;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj != null ? (Boolean) obj : false;
    }

    /**
     * 获取key对应的值
     * @param key
     * @return
     */
    private String get(final String key) {
        Object obj = null;
        try {
            obj = get().execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] data = connection.get(serializer.serialize(key));
                    connection.close();
                    if (data == null) {
                        return null;
                    }
                    return serializer.deserialize(data);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj != null ? obj.toString() : null;
    }

    /**
     * 获取当前值并设置新值
     * @param key
     * @param value
     * @return
     */
    private String getSet(final String key, final String value) {
        Object obj = null;
        try {
            obj = get().execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] ret = connection.getSet(serializer.serialize(key), serializer.serialize(value + ""));
                    connection.close();
                    return serializer.deserialize(ret);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj != null ? (String) obj : null;
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
