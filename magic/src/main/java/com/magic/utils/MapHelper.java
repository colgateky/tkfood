package com.magic.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xm on 2017/11/27.
 */
public class MapHelper<V> {
    private Map<String, V> m;
    public MapHelper(Map<String, V> m) {
        this.m = m;
    }
    public MapHelper() {
        this.m = new HashMap<>();
    }
    public MapHelper set(String key, V value) {
        this.m.put(key, value);
        return this;
    }
    public Integer getAsInteger(String key) {
        return (Integer) this.m.get(key);
    }
    public String getAsString(String key) {
        return (String)this.m.get(key);
    }
    public Double getAsDouble(String key) {
        return (Double)this.m.get(key);
    }
    public Map<String, V> getMap() {
        return this.m;
    }
    public static MapHelper<Object> parseJson(String content) {
        Map<String, Object> m = JsonUtils.parseJson(content, HashMap.class);
        return new MapHelper<>(m);
    }

}
