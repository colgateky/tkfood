package com.tk.center.admin.dto.member;

import com.tk.center.admin.dto.ApiAuthReq;
import com.tk.center.dto.member.MemberQuery;

/**
 * Created by Mingkun on 2020/02/05.
 */
public class ApiMemberQueryReq extends ApiAuthReq {
    protected MemberQuery query;

    public MemberQuery getQuery() {
        return query;
    }

    public void setQuery(MemberQuery query) {
        this.query = query;
    }
}
