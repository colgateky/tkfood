package com.tk.center.admin.dto.member;

import com.tk.center.admin.dto.ApiAuthReq;
import com.tk.center.entity.push.MemberFeedback;

/**
 * Created by Mingkun on 2020/02/06.
 */
public class ApiFeedbackPersistReq extends ApiAuthReq {
    protected MemberFeedback model;

    public MemberFeedback getModel() {
        return model;
    }

    public void setModel(MemberFeedback model) {
        this.model = model;
    }
}
