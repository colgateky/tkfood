package com.tk.center.dto.member;

import com.magic.springboot.dto.PageQuery;
import com.mongodb.DBObject;
import com.tk.center.entity.types.FeedbackStatus;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * Created by Mingkun on 2020/02/06.
 */
public class MemberFeedbackQuery extends PageQuery {
    protected String memberId;
    protected String realName;
    protected String handledUserId;
    protected FeedbackStatus status;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getHandledUserId() {
        return handledUserId;
    }

    public void setHandledUserId(String handledUserId) {
        this.handledUserId = handledUserId;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (!StringUtils.isEmpty(memberId) ) {
            c.and("memberId").is(memberId);
        }
        if (!StringUtils.isEmpty(realName) ) {
            c.and("memberName").regex(realName);
        }
        if (!StringUtils.isEmpty(handledUserId) ) {
            c.and("handledUserId").is(handledUserId);
        }
        if (status != null) {
            c.and("status").is(status);
        }
    }

    @Override
    public void buildSortObject(DBObject sort) {
        super.buildSortObject(sort);
        sort.put("created", -1);
    }
}
