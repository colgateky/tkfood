package com.magic.springboot.dao;

/**
 * Created by xm on 2017/5/19.
 */
public abstract class CollectionIterator<T> {
    public CollectionIterator() {
    }

    public abstract void onFetchOne(T var1);
}
