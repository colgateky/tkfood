package com.tk.center.entity.member;

import com.tk.center.entity.types.TerminalType;

import java.util.Date;

/**
 * Created by Mingkun on 2017/10/13.
 */
public class MemberTmpInfo {
    protected String lastVisitIp;
    protected Date lastVisitTime;
    protected Date lastPurchaseTime; //最後購買時間
    protected String lastPurchaseOrderId; //最後購買訂單
    protected long lastMessageTime;
    protected long registerTime;
    protected TerminalType lastTerminal;

    public String getLastVisitIp() {
        return lastVisitIp;
    }

    public void setLastVisitIp(String lastVisitIp) {
        this.lastVisitIp = lastVisitIp;
    }

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public Date getLastPurchaseTime() {
        return lastPurchaseTime;
    }

    public void setLastPurchaseTime(Date lastPurchaseTime) {
        this.lastPurchaseTime = lastPurchaseTime;
    }

    public String getLastPurchaseOrderId() {
        return lastPurchaseOrderId;
    }

    public void setLastPurchaseOrderId(String lastPurchaseOrderId) {
        this.lastPurchaseOrderId = lastPurchaseOrderId;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public TerminalType getLastTerminal() {
        return lastTerminal;
    }

    public void setLastTerminal(TerminalType lastTerminal) {
        this.lastTerminal = lastTerminal;
    }
}
