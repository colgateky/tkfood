package com.tk.center.entity.record;

import java.util.Date;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class MemberUpgradeRecord extends BaseRecord {
    protected Date upgradeTime;
    protected boolean finish;
    protected String dealer;
    protected int levelNow;
    protected int levelLast;
    protected boolean auto;

    public Date getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(Date upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public int getLevelNow() {
        return levelNow;
    }

    public void setLevelNow(int levelNow) {
        this.levelNow = levelNow;
    }

    public int getLevelLast() {
        return levelLast;
    }

    public void setLevelLast(int levelLast) {
        this.levelLast = levelLast;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }
}
