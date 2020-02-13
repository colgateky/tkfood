package com.tk.center.admin.dto.member;

import com.tk.center.admin.dto.ApiReq;
import com.tk.center.dto.member.MemberMessageQuery;

/**
 * Created by Mingkun on 2020/02/05.
 */
public class ApiMessageQueryReq extends ApiReq {
    protected MemberMessageQuery query;

    public MemberMessageQuery getQuery() {
        return query;
    }

    public void setQuery(MemberMessageQuery query) {
        this.query = query;
    }
}
