package com.magic.utils;

import java.util.UUID;

/**
 * Created by xm on 2017/5/21.
 */
public class UUIDGenerator {
    private UUIDGenerator() {}
    public static String generate() {
        String s = UUID.randomUUID().toString();
        return (s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24)).toUpperCase();
    }
}
