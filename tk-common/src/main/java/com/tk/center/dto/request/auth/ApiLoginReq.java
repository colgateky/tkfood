package com.tk.center.dto.request.auth;

import com.tk.center.dto.request.ApiReq;

/**
 * Created by xm on 2017/5/19.
 */
public class ApiLoginReq extends ApiReq {
    protected String username;
    protected String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
