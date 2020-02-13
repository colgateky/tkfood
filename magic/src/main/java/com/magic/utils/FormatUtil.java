package com.magic.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by ifuck on 2018/1/18.
 */
public class FormatUtil {
    public static String formatMapToString(Map<String, String> map, String connector){
        String ret = "";
        for (Map.Entry<String, String> e : map.entrySet()) {
            String key = e.getKey();
            String value = e.getValue();
            String unit = key + "=" + value + connector;
            ret += unit;
        }
        if (ret.endsWith(connector)){
            ret = ret.substring(0, ret.length() - connector.length());
        }
        return ret;
    }

    public static String convertStreamToString(InputStream stream) throws IOException {
        byte[] buffer = new byte[2048];
        int readBytes = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while((readBytes = stream.read(buffer)) > 0){
            stringBuilder.append(new String(buffer, 0, readBytes));
        }
        return stringBuilder.toString();
    }
}
