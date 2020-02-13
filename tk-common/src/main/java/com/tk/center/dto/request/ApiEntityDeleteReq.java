package com.tk.center.dto.request;

/**
 * Created by ifuck on 2017/6/6.
 */
public class ApiEntityDeleteReq extends ApiAuthReq{
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
