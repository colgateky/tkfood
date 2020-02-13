package com.tk.center.entity.member;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ifuck on 2018/6/10.
 */
public class MemberDto {
    protected String realName;
    protected String phone;
    protected String groupId;
    protected String levelId;
    protected String nickName;
    protected String remark;
    protected List<String> memberTagIds;

    protected BigDecimal totalPurchaseAmount = BigDecimal.ZERO;
    protected int totalPurchaseCount;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getMemberTagIds() {
        return memberTagIds;
    }

    public void setMemberTagIds(List<String> memberTagIds) {
        this.memberTagIds = memberTagIds;
    }

    public BigDecimal getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public int getTotalPurchaseCount() {
        return totalPurchaseCount;
    }

    public void setTotalPurchaseCount(int totalPurchaseCount) {
        this.totalPurchaseCount = totalPurchaseCount;
    }
}
