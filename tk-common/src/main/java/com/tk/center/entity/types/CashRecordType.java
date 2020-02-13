package com.tk.center.entity.types;

/**
 * Created by Mingkun on 2020/02/04.
 */
public enum CashRecordType {
    DEPOSIT("存款"),                    // 存款
    OFFLINE_DEPOSIT("线下存款"),        // 线下存款
    WITHDRAW("取款"),                  // 取款
    TRANSFER_IN("转入"),            // 转账
    TRANSFER_OUT("转出"),            // 转账
    TRANSFER_ROLLBACK("转入回滚"),     // 转账回滚
    BET("下注"),                      // 下注
    BETWIN("中奖"),                   // 中奖，包括赢与平
    WITHDRAW_ROLLBACK("取款回滚"),
    PROMOTION("优惠奖励"),
    DEPOSIT_RETURN("存款返利"),
    CASH_REBATE("返水"),
    LOSE_CASH_REBATE("救援金"),
    THIRD_CASH_REBATE("第三方返水"),
    FIX("修正"),
    LUCKY_DRAW_HIT("中奖"),
    REC_CASH_REBATE("分销返水"),
    RE_DEAL_ISSUE("彩票二次结算"),

    GRAB_RED_PACKET("抢红包"),
    SEND_RED_PACKET("发红包"),
    RED_PACKET_BOMB("红包中雷"),
    RED_PACKET_SEND_HIT("中雷奖励(禁抢)"),
    RED_PACKET_GRAB_HIT("中雷奖励(扫雷)"),
    RED_PACKET_BONUS("抢包奖励"),
    RED_PACKET_BOMB_BONUS("红包中雷奖励"),
    RED_PACKET_REBATE("红包返佣"),
    RED_PACKET_CASH_REBATE("红包返水"),
    RED_PACKET_DAY_BONUS("日玩法奖励"),
    RED_PACKET_DEPOSIT("发红包押金"),
    RED_PACKET_RECORD_DEPOSIT("抢红包押金"),
    RED_PACKET_DEPOSIT_RETURN("退还红包押金"),
    RED_PACKET_BULL("红包牛牛结算"),

    THIRD_RED_TRANSFER("红包转账"),

    CANCEL_BET_RECORD("取消注单");

    private String name;
    CashRecordType(String str) {
        this.name = str;
    }
    public String toString() {
        return name;
    }

    public String key(){
        return super.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
