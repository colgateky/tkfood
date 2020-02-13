package com.tk.center.dto.request.auth;

import com.tk.center.dto.request.ApiAuthReq;
import com.magic.springboot.entity.User;

/**
 * Created by ifuck on 2017/5/26.
 */
public class ApiUserPersistReq extends ApiAuthReq {
    protected User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
