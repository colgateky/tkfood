package com.magic.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;



/**
 * Created by xyz on 2015/6/1.
 */
public class JsonUtils {
    private static SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static <T> T parseJson(String json, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(dtFormat);
        T ret = null;
        try {
            ret = mapper.readValue(json, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static String toJson2(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
    //值为空也序列化
    public static String toJsonWithNull(Object object){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(object);
    }
    //json转对象
    public static <T> T parseJson2(String json, Class<T> cls){
        return new Gson().fromJson(json, cls);
    }
    public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
    public static <T> List<T> parseJsonList(String json, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(dtFormat);
        List<T> ret = null;
        try {
            JavaType javaType = getCollectionType(mapper, ArrayList.class, cls);
            ret =  (List<T>)mapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static String toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(dtFormat);
        String ret = null;
        try {
            ret = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    //JSON TO MAP
    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public final static boolean isJsonValid(String jsonString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
