package com.magic.utils;

import com.magic.springboot.SpringBeanUtils;
import com.magic.springboot.client.Client;

import java.util.Date;

/**
 * Created by xyz on 2016/11/23.
 */
public class LogHelper {
    private String tag;
    public LogHelper(String tag) {
        this.tag = tag;
    }
    private void print(String ii, String msg) {
        System.out.println(DateUtils.getDateTimeStr(new Date()) + " " + ii + " " + tag + " " + msg);
    }
    private void printWithClient(String ii, String msg){
        Client client = null;
        client = SpringBeanUtils.getBean(Client.class);
        String clientId = "NOCLIENT";
        if (client != null){
            clientId = client.getClientId();
        }
        System.out.println(clientId + " " + DateUtils.getDateTimeStr(new Date()) + " " + ii + " " + tag + " " + msg);
    }
    public void info(String msg) {
        print("INFO", msg);
    }
    public void warn(String msg) {
        print("WARN", msg);
    }
    public void error(String msg) {
        print("ERROR", msg);
    }

    public void infoWithClient(String msg) {
        printWithClient("INFO", msg);
    }
    public void warnWithClient(String msg) {
        printWithClient("WARN", msg);
    }
    public void errorWithClient(String msg) {
        printWithClient("ERROR", msg);
    }
}
