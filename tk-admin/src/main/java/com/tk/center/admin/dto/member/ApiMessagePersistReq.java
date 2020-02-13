package com.tk.center.admin.dto.member;

import com.tk.center.admin.dto.ApiReq;
import com.tk.center.entity.push.MemberMessage;

/**
 * Created by Mingkun on 2020/02/05.
 */
public class ApiMessagePersistReq extends ApiReq {
    protected MemberMessage model;

    public MemberMessage getModel() {
        return model;
    }

    public void setModel(MemberMessage model) {
        this.model = model;
    }
}
