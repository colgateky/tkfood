package com.tk.center.dto.response.auth;

import com.tk.center.dto.response.ApiResp;
import com.magic.springboot.entity.User;

/**
 * Created by ifuck on 2017/5/26.
 */
public class ApiUserPersistResp extends ApiResp {
    protected User model;

    public User getModel() {
        return model;
    }

    public void setModel(User model) {
        this.model = model;
    }
}
