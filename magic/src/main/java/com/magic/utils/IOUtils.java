package com.magic.utils;

import java.io.*;

/**
 * Created by xxm on 16/7/15.
 */
public class IOUtils {
    public static boolean writeFile(File f, String content) {
        return writeFile(f, content.getBytes());
    }
    public static boolean writeFile(File f, byte[] data) {
        boolean ret = false;
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(f);
            fout.write(data);
            ret = true;
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
    public static String readTxtFile(File f) {
        byte[] ret = readFile(f);
        if (ret == null) return null;
        return new String(ret);
    }
    public static byte[] readFile(File f) {
        if (f.exists() == false) return null;
        byte[] ret = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(f);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            while (true) {
                int len = fin.read(buff);
                if (len <= 0) break;
                bout.write(buff, 0, len);
            }
            ret = bout.toByteArray();
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
    public static String readTxtFile(InputStream ins) {
        return new String(readFile(ins));
    }
    public static byte[] readFile(InputStream ins) {
        byte[] ret = null;
        InputStream fin = ins;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            while (true) {
                int len = fin.read(buff);
                if (len <= 0) break;
                bout.write(buff, 0, len);
            }
            ret = bout.toByteArray();
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    public static InputStream byteToInputStream(byte[] data){
        return new ByteArrayInputStream(data);
    }
}
