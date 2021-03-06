package com.tk.center.dto.response;

/**
 * Created by xm on 2017/5/19.
 */
public class ApiResp {
    protected int status;
    protected String token;
    protected String message;
    protected long time = System.currentTimeMillis();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
