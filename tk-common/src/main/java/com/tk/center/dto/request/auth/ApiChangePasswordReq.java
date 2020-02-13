package com.tk.center.dto.request.auth;

import com.tk.center.dto.request.ApiReq;

/**
 * Created by ifuck on 2017/6/18.
 */
public class ApiChangePasswordReq extends ApiReq {
    protected String userId;
    protected String oldPassword;
    protected String newPassword;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
