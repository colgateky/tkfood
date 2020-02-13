package com.tk.center.common;


import com.magic.springboot.exception.ApiException;

/**
 * Created by tt on 2017/3/23.
 */
public class ApiErrorCode {
    public static final int SUCCESS = 0;

    private static ApiException create(int code, String message) {
        return new ApiException(code, message);
    }

    /**
     * -1 ~ -100 为异常
     */

    public static final ApiException FILE_UPLOAD_ERROR = create(-20, "上传文件出错");

    /**
     * 1000 ~ 2000 为业务错误
     */
    public static final ApiException PARAMS_IS_NULL = create(1000, "参数为空");
    public static final ApiException NO_AUTH = create(1001, "登录失效，请重新登录");
    public static final ApiException PLAYER_NOT_FOUND = create(1002, "Illegal,非法访问");
    public static final ApiException GAME_NOT_FOUND = create(1003, "游戏不存在");
    public static final ApiException PLAYER_LOGIN_FAILED = create(1004, "用户名或密码错误");
    public static final ApiException PLAYER_NOT_ENOUGH = create(1005, "您的余额不足");
    public static final ApiException DATA_NOT_FOUND = create(1006, "数据不存在");
    public static final ApiException REGISTER_FAILED = create(1007, "用户注册失败");
    public static final ApiException CHANGE_PWD_FAILED = create(1008, "用户密码");
    public static final ApiException BIND_PLAYER_BANK_ACCOUNT_FAILED = create(1009, "绑定银行卡失败");
    public static final ApiException LOTTERY_STATUS_NOT_FOUND = create(1010, "开奖状态不存在");
    public static final ApiException PLAY_RULE_NOT_FOUND = create(1011, "玩法规则不存在");
    public static final ApiException LOTTERY_DRAW_NOT_FOUND = create(1012, "当期彩票不存在");
    public static final ApiException LOTTERY_NOT_IN_BET = create(1012, "彩票已封盘或结束，不能下注");
    public static final ApiException DEPOSIT_FAILED = create(1013, "充值失败");
    public static final ApiException DEPOSIT_INFO_MISS = create(1014, "信息不全");
    public static final ApiException PAY_METHOD_ERROR = create(1015, "支付方式错误");
    public static final ApiException PARAMS_ERROR = create(1016, "参数错误");
    public static final ApiException GENERATE_BET_RECORDS_FAILED = create(1017, "生成投注记录失败");
    public static final ApiException AVATAR_UPLOAD_FAILED = create(1018, "头像上传失败");
    public static final ApiException WITHDRAW_FAILED = create(1019, "取款失败");
    public static final ApiException GAME_PLATFORM_BALANCE_QUERY_FAILED = create(1020, "平台余额获取失败");
    public static final ApiException VERIFYCODE_EXPIRED = create(1021, "验证码超时");
    public static final ApiException VERIFYCODE_ERROR = create(1022, "验证码错误");
    public static final ApiException RECEIVE_CASH_REBATE_FAILED = create(1023, "领取返水失败");
    public static final ApiException RECORD_EXIST = create(1024, "记录已存在");
    public static final ApiException PROMOTION_UN_START = create(1025, "活动尚未开始");
    public static final ApiException PROMOTION_RECEIVE_FAILED = create(1026, "优惠领取失败");
    public static final ApiException PlAYER_DISABLED = create(1027, "账号已停用");
    public static final ApiException UN_CONFIG = create(1028, "未设置");
    public static final ApiException RECORD_NOT_FOUND = create(1029, "记录不存在");
    public static final ApiException TRANSFER_FAILED = create(1030, "户内转账失败");
    public static final ApiException UPDATE_PLAYER_INFO_FAILED = create(1031, "完善信息失败");
    public static final ApiException PROMOTION_APPLY_FAILED = create(1032, "优惠活动申请失败");
    public static final ApiException DEPOSIT_CALLBACK_FAILED = create(1033, "存款回调失败");
    public static final ApiException PAYOUT_CALLBACK_FAILED = create(1034, "代付回调失败");
    public static final ApiException PAYOUT_FAILED = create(1035, "代付失败");
    public static final ApiException REMOTE_ERROR = create(1036, "远程调用出错");
    public static final ApiException THIRD_TRANSFER_LIMIT_ERROR = create(1037, "第三方转入额度错误");

    //admin
    public static ApiException IP_FAILED = new ApiException(10003, "IP限制访问");
    public static ApiException REGISTER_INFO_MISS = new ApiException(10004, "注册信息不全");
    public static ApiException REGISTER_USERNAME_EXIST = new ApiException(10005, "用户名已存在");
    public static ApiException TEST_USER_THIRD_DENY = new ApiException(10006, "试玩用户暂不支持参与第三方游戏");
    public static ApiException TEST_USER_FUND_DEDENY = new ApiException(10007, "试玩用户暂不支持资金操作");
    public static ApiException TEST_USER_BET_DEDENY = new ApiException(10008, "试玩用户暂不支持投注");
    public static ApiException TEST_USER_DEDENY = new ApiException(10009, "试玩用户暂不支持该操作");
    public static ApiException PERSIST_ROLE_FAILED = new ApiException(10013, "角色信息更新失败");
    public static ApiException PERSIST_USER_FAILED = new ApiException(10014, "用户信息更新失败");
    public static final ApiException AUTH_EXPIRED = create(10015, "超时，重新登录");
    public static ApiException LOGIN_FAILED = new ApiException(10016, "登录失败");
    public static ApiException AUTH_FAILED = new ApiException(10017, "未授权非法访问");
    public static ApiException PERSIST_THIRD_PAY_FAILED = new ApiException(10021, "支付方式更新失败");
    public static ApiException PERSIST_BANK_FAILED = new ApiException(10022, "银行信息更新失败");
    public static ApiException PERSIST_ADMINBANKACCOUNT_FAILED = new ApiException(10023, "管理账户信息更新失败");
    public static ApiException CUSTOM_FAILED = new ApiException(10024, "操作失败");
    public static ApiException PERSIST_FAILED = new ApiException(10025, "数据持久化失败");
    public static ApiException PERSIST_FILERESOURCE_FAILED = new ApiException(10018, "文件资源更新失败");
    public static ApiException PERSIST_FILERESOURCELIBRARY_FAILED = new ApiException(1019, "资源库信息更新失败");
    public static ApiException RESIZE_IMAGE_FAILED = new ApiException(10020, "图片大小缩放失败");
    public static ApiException RESOURCE_GET_FAILED = new ApiException(10021, "资源获取失败");
    public static ApiException DELETE_FAILED = new ApiException(10026, "数据删除失败");
    public static ApiException REPORT_STAT_ERROR = new ApiException(10027, "报表统计错误");
    public static ApiException USER_ERROR = new ApiException(10028, "用户错误");
    public static ApiException IMPORT_ERROR = new ApiException(10029, "导入错误");

    //agent
    public static ApiException AGENT_ERROR = new ApiException(11001, "代理错误");
    public static ApiException AGENT_CASH_ERROR = new ApiException(11001, "代理资金错误");

    //third
    public static ApiException THIRD_PLATFORM_ERROR = new ApiException(20001, "第三方平台错误");


    //红包错误
    public static ApiException RED_NOT_ENOUGH_BALANCE = new ApiException(30001, "您的余额不足");
    public static ApiException RED_PACKET_FINISH = new ApiException(30002, "红包已抢完");
    public static ApiException RED_PACKET_GRABS = new ApiException(30003, "重复抢包");
    public static ApiException RED_PACKET_NO_BOMB = new ApiException(30004, "雷配置缺失，请联系客服");
    public static ApiException RED_PACKET_NOT_DEAL = new ApiException(30005, "您还有未结算的红包，您耐心等待。");
    public static ApiException RED_MAINTAIN = new ApiException(30006, "红包系统暂时维护中，请稍后重试");

    //红包第三方接口错误
    public static ApiException RED_THIRD_PARAM_MISS = new ApiException(50010, "参数缺失");
    public static ApiException RED_THIRD_PARAM_WRONG = new ApiException(50011, "参数错误");
    public static ApiException RED_THIRD_PLAYER_EXIST = new ApiException(50012, "用户名已注册");
    public static ApiException RED_THIRD_WRONG_PASSWORD = new ApiException(50013, "密码错误");
    public static ApiException RED_THIRD_NOT_ENOUGH_BALANCE = new ApiException(50014, "余额不足");
    public static ApiException RED_THIRD_PLAYER_NOT_EXIST = new ApiException(50015, "会员不存在");

    public static ApiException RED_THIRD_CLIENT_NOT_EXIST = new ApiException(50000, "前缀不存在");
    public static ApiException RED_THIRD_KEY_NOT_MATCH = new ApiException(50001, "密钥不匹配");
    public static ApiException RED_THIRD_IP_NOT_WHITE = new ApiException(50002, "IP不在白名单内");


    public static void throwError(String message) {
        ApiException ret = new ApiException(999, message);
        ret.throwSelf();
    }
}
