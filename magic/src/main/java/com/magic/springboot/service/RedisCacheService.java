package com.magic.springboot.service;


import java.util.Map;
import java.util.Set;

/**
 * Created by xm on 2017/5/20.
 */
public interface RedisCacheService {
    void hashSet(String key, String hashKey, Object value);
    <T> Map<String, T> hashGetAll(String key, Class<T> cls);
    Set<String> hashGetAllKeys(String key);
    boolean hashSetIf(String key, String hashKey, Object v);
    long hashCount(String key);
    <T> T hashGet(String key, String hashKey, Class<T> cls);
    void hashDel(String key, String hashKey);
    void hashDelAll(String key);
    void lpush(String key, Object value);
    <T> T rpop(String key, Class<T> cls);
    Long getTime();

    void setTmp(String key, String value, long timeoutSecs);
    String getTmp(String key);
    void setTmpObject(String key, Object value, long timeoutSecs);
    <T> T getTmpObject(String key, Class<T> cls);
    Set<String> getAllKeys(String pattern);

}
