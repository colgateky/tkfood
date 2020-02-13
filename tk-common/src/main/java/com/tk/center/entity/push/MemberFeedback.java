package com.tk.center.entity.push;

import com.tk.center.entity.BaseModel;
import com.tk.center.entity.types.FeedbackStatus;

import java.util.Date;

/**
 * Created by Mingkun on 2020/02/05.
 * 玩家留言
 */
public class MemberFeedback extends BaseModel {
    protected String memberId;
    protected String memberName;
    protected String memberPhone;
    protected String type;
    protected String title;
    protected String content;
    protected String handledUserId;
    protected Date handledTime;
    protected String remark;
    protected FeedbackStatus status;
    protected String ip;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHandledUserId() {
        return handledUserId;
    }

    public void setHandledUserId(String handledUserId) {
        this.handledUserId = handledUserId;
    }

    public Date getHandledTime() {
        return handledTime;
    }

    public void setHandledTime(Date handledTime) {
        this.handledTime = handledTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
