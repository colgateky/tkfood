package com.tk.center.framework;

import com.magic.utils.CryptUtils;

/**
 * Created by xm on 2017/5/20.
 */
public class TokenHelper {
    public static String decryptToken(String token) {
        byte[] rawData = CryptUtils.aesDecrypt(CryptUtils.fromHexString(token), "gTRxBTmb1s");
        if (rawData == null) return null;
        String raw = new String(rawData);
        String[] items = raw.split("#");
//        long t = Long.parseLong(items[0]);
//        if (System.currentTimeMillis() - t > 2 * 60 * 60 * 1000) {
//            ApiErrorCode.AUTH_EXPIRED.throwSelf();
//        }
        String userId = items[1];
        return userId;
    }

    public static String generateToken(String userId) {
        String raw = System.currentTimeMillis() + "#" + userId;
        byte[] encrypted = CryptUtils.aesEncrypt(raw.getBytes(), "gTRxBTmb1s");
        return CryptUtils.toHexString(encrypted);
    }

    public static String generateSecretToken(String content){
        String raw = System.currentTimeMillis() + "#" + content;
        byte[] encrypted = CryptUtils.aesEncrypt(raw.getBytes(), "xxyyTigerQQt");
        return CryptUtils.toHexString(encrypted);
    }

    public static String decryptSecretToken(String token) {
        byte[] rawData = CryptUtils.aesDecrypt(CryptUtils.fromHexString(token), "xxyyTigerQQt");
        if (rawData == null) return null;
        String raw = new String(rawData);
        String[] items = raw.split("#");
        String ret = items[1];
        return ret;
    }
}
