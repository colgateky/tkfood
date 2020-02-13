package com.tk.center.admin.dto.member;

import com.tk.center.admin.dto.ApiReq;
import com.tk.center.dto.member.MemberFeedbackQuery;

/**
 * Created by Mingkun on 2020/02/06.
 */
public class ApiMemberFeedbackQueryReq extends ApiReq {
    protected MemberFeedbackQuery query;

    public MemberFeedbackQuery getQuery() {
        return query;
    }

    public void setQuery(MemberFeedbackQuery query) {
        this.query = query;
    }
}
