package com.tk.center.dto.response.auth;

import com.tk.center.dto.response.ApiResp;
import com.magic.springboot.entity.Role;

/**
 * Created by ifuck on 2017/5/26.
 */
public class ApiRolePersistResp extends ApiResp {
    protected Role model;

    public Role getModel() {
        return model;
    }

    public void setModel(Role model) {
        this.model = model;
    }
}
