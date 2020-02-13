package com.magic.utils;

import com.magic.utils.rsa.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.Formatter;
import java.util.Map;

/**
 * Created by xyz on 2015/7/10.
 */
@SuppressWarnings("ALL")
public class CryptUtils {
    public static String getMd5(String raw){
        return getMd5(raw.getBytes());
    }

    public static String getMd5(String raw, String encoding){
        try{
            String ret = getMd5(raw.getBytes(encoding));
            return ret;
        }catch (Exception e){
            return null;
        }
    }

    public static String getMd5(byte[] charArray){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toUpperCase();

    }

    public static byte[] base64Decode(String s) {
        try {
            return Base64.getDecoder().decode(s);
        } catch (Exception exp) {
            return null;
        }
    }
    public static String getBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    public static String getBase64(String s) {
        if (s == null) return null;
        return Base64.getEncoder().encodeToString(s.getBytes());
    }
    public static String xor(String orig, int code) {
        byte[] data = fromHexString(orig);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte)(data[i] ^ code);
        }
        try {
            return new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    public static byte[] fromHexString(String hexstring) {
        byte[] inputByteArray = new byte[hexstring.length() / 2];
        for (int x = 0; x < hexstring.length() / 2; x++) {
            byte b = (byte) Integer.parseInt(
                    hexstring.substring(x * 2, x * 2 + 2), 16);
            inputByteArray[x] = b;
        }
        return inputByteArray;
    }
    public static byte[] aesEncrypt(byte[] byteContent, String password) {
        try {
            byte[] enCodeFormat = initPasswordEnCode(password);
            return aesEncryptByPasswordEnCode(byteContent, enCodeFormat);
        } catch(Exception exp) {
            exp.printStackTrace();
            return null;
        }
    }
    public static byte[] aesEncrypt(String plainText, String key, String ivStr) throws Exception {
        byte[] clean = plainText.getBytes();

        // Generating IV.
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        System.arraycopy(ivStr.getBytes(), 0, iv, 0, ivStr.length());

        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivStr.getBytes());

        // Hashing key.
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        digest.update(key.getBytes("UTF-8"));
        byte[] keyBytes = new byte[16];
        System.arraycopy(key.getBytes(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Encrypt.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);
        return encrypted;
        // Combine IV and encrypted part.
//        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
//        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
//        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);

//        return encryptedIVAndText;
    }

    public static String aesDecrypt(byte[] encryptedIvTextBytes, String key, String ivStr) throws Exception {
        int ivSize = 16;
        int keySize = 16;

        // Extract IV.
        byte[] iv = new byte[ivSize];
        System.arraycopy(ivStr.getBytes(), 0, iv, 0, ivStr.length());

        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivStr.getBytes());

        // Hashing key.
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        digest.update(key.getBytes("UTF-8"));
        byte[] keyBytes = new byte[16];
        System.arraycopy(key.getBytes(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");


        // Decrypt.
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipherDecrypt.doFinal(encryptedIvTextBytes);

        return new String(decrypted);
    }

    private static byte[] initPasswordEnCode(String password) throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
        secureRandom.setSeed(password.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] aesEncryptByPasswordEnCode(byte[] byteContent, byte[] passwordEnCode) {
        try {
            byte[] enCodeFormat = passwordEnCode;
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] aesDecrypt(byte[] content, String password) {
        try {
            byte[] enCodeFormat = initPasswordEnCode(password);
            return aesDecryptByPasswordEnCode(content, enCodeFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] aesDecryptByPasswordEnCode(byte[] content, byte[] passwordEncode) {
        try {
            byte[] enCodeFormat = passwordEncode;
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public final static String getFileMd5(String filePath) {
        FileInputStream fins = null;
        try {
            fins = new FileInputStream(filePath);
            return getMd5(fins);
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            if (fins != null) {
                try {
                    fins.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public final static String getMd5(InputStream ins) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            byte[] data = new byte[10240];
            while (true) {
                int len = ins.read(data);
                if (len <= 0) break;
                mdInst.update(data, 0, len);
            }
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String SHA_256 = "SHA-256";
    private static final String SHA_1 = "SHA-1";

    public static String sha256(String input) {
        return hash(input, SHA_256);
    }

    public static String sha1(String input) {
        return hash(input, SHA_1);
    }

    private static String hash(String input, String hashAlgorithm) {
        if (input == null) {
            return null;
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(hashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("this can't happen", e);
        }
        byte[] hash = md.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static String desEncrypt(String input, String key){
        try {
            return getBase64(desEncrypt(input.getBytes(), key)).replaceAll("\\s*", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String desDecrypt(String input, String key){

        byte[] result=base64Decode(input);
        try {
            return new String(desDecrypt(result, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] desEncrypt(byte[] plainText, String key)throws Exception{
        SecureRandom sr=new SecureRandom();
        DESKeySpec dks=new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
        byte data[] = plainText;
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;
    }

    public static byte[] desDecrypt(byte[] encryptText, String key)throws Exception{
        SecureRandom sr=new SecureRandom();
        DESKeySpec dks=new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
        byte encryptedData[] = encryptText;
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return decryptedData;
    }

    public static byte[] rsaDecrypt(byte[] data, String privateKeyValue) throws Exception {
        byte[] key = Base64.getDecoder().decode(privateKeyValue);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int blockSize = cipher.getOutputSize(data.length);
        return doFinal(data, cipher, blockSize);
    }

    public static byte[] rsaEncrypt(byte[] data, String publicKey) throws Exception {
        byte[] key = Base64.getDecoder().decode(publicKey);
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        int blockSize = cipher.getOutputSize(data.length) - 11;
        return doFinal(data, cipher, blockSize);
    }

    /**
     * RSA Signature
     * @param content: Signature data to be signed
     * @param privateKey: Merchant private key
     * @param encode: Character set coding
     * @return Signature value
     */
    public static String rsaEncrypt(String content, String privateKey)
    {
        try
        {
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( base64Decode(privateKey) );
            KeyFactory keyf                 = KeyFactory.getInstance("RSA");
            PrivateKey priKey               = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update( content.getBytes());

            byte[] signed = signature.sign();
            return getBase64(signed);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * RSA validate signature
     * @param content: Signature data to be signed
     * @param sign: Signature value
     * @param publicKey: merchant's public key
     * @param encode: Character set coding
     * @return boolean
     */
    public static boolean rsaVerifySign(String content, String sign, String publicKey)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = base64Decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature
                    .getInstance("SHA1WithRSA");
            signature.initVerify(pubKey);
            signature.update( content.getBytes() );
            boolean bverify = signature.verify( base64Decode(sign) );
            return bverify;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private static byte[] doFinal(byte[] decryptData, Cipher cipher, int blockSize)
            throws Exception {
        int offSet = 0;
        byte[] cache = null;
        int i = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while (decryptData.length - offSet > 0) {
            if (decryptData.length - offSet > blockSize) {
                cache = cipher.doFinal(decryptData, offSet, blockSize);
            } else {
                cache = cipher.doFinal(decryptData, offSet, decryptData.length - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * blockSize;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

}
