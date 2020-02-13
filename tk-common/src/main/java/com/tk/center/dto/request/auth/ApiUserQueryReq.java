package com.tk.center.dto.request.auth;

import com.tk.center.dto.request.ApiAuthReq;
import com.magic.springboot.dto.UserQuery;

/**
 * Created by xm on 2017/5/24.
 */
public class ApiUserQueryReq extends ApiAuthReq {
    protected UserQuery query;

    public UserQuery getQuery() {
        return query;
    }

    public void setQuery(UserQuery query) {
        this.query = query;
    }
}
