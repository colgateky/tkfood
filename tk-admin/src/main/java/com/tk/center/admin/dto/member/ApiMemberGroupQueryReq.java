package com.tk.center.admin.dto.member;

import com.tk.center.dto.member.MemberGroupQuery;

public class ApiMemberGroupQueryReq {

    protected MemberGroupQuery query;

    public MemberGroupQuery getQuery() {
        return query;
    }

    public void setQuery(MemberGroupQuery query) {
        this.query = query;
    }
}
