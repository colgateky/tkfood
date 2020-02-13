package com.tk.center.dto.request.auth;

import com.tk.center.dto.request.ApiAuthReq;

/**
 * Created by ifuck on 2017/5/26.
 */
public class ApiUserDeleteReq extends ApiAuthReq {
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
