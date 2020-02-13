package com.magic.springboot.dao;

/**
 * Created by xm on 2017/10/16.
 */
public abstract class CollectionBreakIterator<T> {
    /**
     * 返回true则打断数据获取
     * @param var1
     * @return
     */
    public abstract boolean onFetchOne(T var1);
}
