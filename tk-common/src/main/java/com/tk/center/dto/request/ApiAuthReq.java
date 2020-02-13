package com.tk.center.dto.request;

import com.tk.center.common.ApiErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.magic.springboot.auth.pojo.LoginUser;

/**
 * Created by xm on 2017/5/19.
 */
public class ApiAuthReq extends ApiReq {

    @JsonIgnore
    protected LoginUser loginUser;

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    public void checkPermission(String resource) {
        boolean tag = loginUser.check(resource);
        if (!tag) {
            ApiErrorCode.AUTH_FAILED.throwSelf();
        }
    }
}
