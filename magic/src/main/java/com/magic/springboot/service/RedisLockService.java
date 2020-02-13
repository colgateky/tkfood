package com.magic.springboot.service;

/**
 * Created by tt on 2017/3/24.
 */
public interface RedisLockService {
    /**
     * 无法获取锁时，会返回false, 获取成功返回true, 用于高频度的锁，比如记录id递增
     * @param key
     * @param flow
     * @return
     */
    boolean lockNoError(String key, LockFlow flow);

    /**
     * 无法获取到锁时，会触发异常，用于存款，投注，用户相关锁用户id的场景
     * @param key
     * @param flow
     */
    void lock(String key, LockFlow flow);
    void lock(String key, long timeoutMsecs, LockFlow flow);
}
