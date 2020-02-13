package com.magic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jamen on 2017/3/5.
 */
public class DateDeserializer {
    private final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private final SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
    private final SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy/MM/dd");
    private final SimpleDateFormat formatter5 = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat formatter6 = new SimpleDateFormat("yyyyMMdd");


    public Date deserialize(String source) {

        try {
            if (source.length() == 8) {
                return formatter6.parse(source);
            }
            if (source.length() == 14) {
                return formatter3.parse(source);
            }
            if (source.length() == 10) {
                if (source.contains("/")) {
                    return formatter4.parse(source);
                } else {
                    return formatter5.parse(source);
                }
            }
            if (source.contains("/")) {
                return formatter1.parse(source);
            } else {
                return formatter2.parse(source);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }
}
