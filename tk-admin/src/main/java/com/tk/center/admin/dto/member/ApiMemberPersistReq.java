package com.tk.center.admin.dto.member;

import com.tk.center.admin.dto.ApiAuthReq;
import com.tk.center.entity.member.Member;

/**
 * Created by Mingkun on 2020/02/06.
 */
public class ApiMemberPersistReq extends ApiAuthReq {
    protected Member model;

    public Member getModel() {
        return model;
    }

    public void setModel(Member model) {
        this.model = model;
    }

}
