package com.tk.center.entity.member;

import com.tk.center.entity.BaseModel;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xm on 2017/8/12.
 */
@Document
public class MemberInfo extends BaseModel {
    //記錄第一次、二次、三次購買的金額與時間
    protected BigDecimal firstPurchaseAmount;
    protected Date firstPurchaseTime;
    protected BigDecimal secondPurchaseAmount;
    protected Date secondPurchaseTime;
    protected BigDecimal thirdPurchaseAmount;
    protected Date thirdPurchaseTime;

    protected BigDecimal totalPurchaseAmount;
    @Indexed
    protected int totalPurchaseCount;
    protected Date lastPurchaseTime;

    public BigDecimal getFirstPurchaseAmount() {
        return firstPurchaseAmount;
    }

    public void setFirstPurchaseAmount(BigDecimal firstPurchaseAmount) {
        this.firstPurchaseAmount = firstPurchaseAmount;
    }

    public Date getFirstPurchaseTime() {
        return firstPurchaseTime;
    }

    public void setFirstPurchaseTime(Date firstPurchaseTime) {
        this.firstPurchaseTime = firstPurchaseTime;
    }

    public BigDecimal getSecondPurchaseAmount() {
        return secondPurchaseAmount;
    }

    public void setSecondPurchaseAmount(BigDecimal secondPurchaseAmount) {
        this.secondPurchaseAmount = secondPurchaseAmount;
    }

    public Date getSecondPurchaseTime() {
        return secondPurchaseTime;
    }

    public void setSecondPurchaseTime(Date secondPurchaseTime) {
        this.secondPurchaseTime = secondPurchaseTime;
    }

    public BigDecimal getThirdPurchaseAmount() {
        return thirdPurchaseAmount;
    }

    public void setThirdPurchaseAmount(BigDecimal thirdPurchaseAmount) {
        this.thirdPurchaseAmount = thirdPurchaseAmount;
    }

    public Date getThirdPurchaseTime() {
        return thirdPurchaseTime;
    }

    public void setThirdPurchaseTime(Date thirdPurchaseTime) {
        this.thirdPurchaseTime = thirdPurchaseTime;
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

    public Date getLastPurchaseTime() {
        return lastPurchaseTime;
    }

    public void setLastPurchaseTime(Date lastPurchaseTime) {
        this.lastPurchaseTime = lastPurchaseTime;
    }
}
