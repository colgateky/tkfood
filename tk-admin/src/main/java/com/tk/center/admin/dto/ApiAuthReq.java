package com.tk.center.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.magic.springboot.auth.pojo.LoginUser;
import com.tk.center.common.ApiErrorCode;

/**
 * Created by Mingkun on 2020/02/03.
 */
public class ApiAuthReq extends ApiReq{

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
