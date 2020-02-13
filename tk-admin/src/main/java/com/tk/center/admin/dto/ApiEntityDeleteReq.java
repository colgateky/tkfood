package com.tk.center.admin.dto;

/**
 * Created by Mingkun on 2020/02/03.
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
