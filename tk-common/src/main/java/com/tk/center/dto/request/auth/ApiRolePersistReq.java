package com.tk.center.dto.request.auth;

import com.tk.center.dto.request.ApiAuthReq;
import com.magic.springboot.entity.Role;

/**
 * Created by ifuck on 2017/5/26.
 */
public class ApiRolePersistReq extends ApiAuthReq {
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    protected Role role;

}
