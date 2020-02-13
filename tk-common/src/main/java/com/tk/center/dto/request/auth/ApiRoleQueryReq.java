package com.tk.center.dto.request.auth;

import com.tk.center.dto.request.ApiAuthReq;
import com.magic.springboot.dto.RoleQuery;

/**
 * Created by ifuck on 2017/5/26.
 */
public class ApiRoleQueryReq extends ApiAuthReq {
    protected RoleQuery query;

    public RoleQuery getQuery() {
        return query;
    }

    public void setQuery(RoleQuery query) {
        this.query = query;
    }

}
