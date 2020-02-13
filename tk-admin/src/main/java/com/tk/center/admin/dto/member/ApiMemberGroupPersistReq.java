package com.tk.center.admin.dto.member;

import com.tk.center.admin.dto.ApiAuthReq;
import com.tk.center.entity.member.MemberGroup;

/**
 * Created by Mingkun on 2020/02/03.
 */
public class ApiMemberGroupPersistReq extends ApiAuthReq {
    protected MemberGroup model;

    public MemberGroup getModel() {
        return model;
    }

    public void setModel(MemberGroup model) {
        this.model = model;
    }
}
