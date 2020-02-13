package com.tk.center.dto.response.auth;

/**
 * Created by xm on 2017/5/19.
 */
public class ApiLoginResp extends ApiUserInfoResp {
    protected int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
