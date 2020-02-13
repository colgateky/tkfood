package com.tk.center.common;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class Constant {
    /**
     * 系统配置部分
     */

    //IP开放地区
    public static final String IP_AVAILABLE_AREA = "ip_available_area";
    //IP黑名单
    public static final String IP_DENY = "ip_deny";
    //IP白名单
    public static final String IP_ALLOW = "ip_allow";
    //同一终端的单点登录
    public static final String SINGLE_LOGIN_WITH_PLATFORM = "single_login_with_platform";
    //不区分终端的单点登录
    public static final String SINGLE_LOGIN = "single_login";
    //管理端单点登录
    public static final String ADMIN_SINGLE_LOGIN = "admin_single_login";
    //计划期次初始配置
    public static final String PLAN_FIRST_ISSUE = "plan_first_issue";
    //玩家是否自动转账
    public static final String PLAYER_AUTO_TRANSFER = "player_auto_transfer";
    //demo站地址
    public static final String DEMO_URL = "demo_url";
    //CDN地址
    public static final String CDN = "cdn";
    //域名
    public static final String DOMAINS = "domains";
    //APP ID
    public static final String APP_BUNDLE_ID = "app_bundle_id";
    //APP首次下载对应优惠活动
    public static final String APP_DOWNLOAD_PROMOTION = "app_download_promotion";

    //APP安装包下载地址
    public static final String DOWNLOAD_APP_URL = "download_app_url";
    //启用所有终端的彩票额外信息api
    public static final String ENABLE_ALL_EXTRA = "enable_all_extra";
    //给第三方APP调用的域名
    public static final String EXTRA_APP_DOMAIN = "extra_app_domain";
    public static final String EXTRA_SITE_ID = "extra_site_id";

    /**
     * 会员区块
     */

    //修改备注、渠道号
    public static final String PLAYER_SHOW_REMARK_CHANNEL = "player_show_remark_channel";
    //银行卡绑定张数上限
    public static final String PLAYER_BANKCARD_COUNT_LIMIT = "player_bankcard_count_limit";
    //是否发放分销返水
    public static final String ENABLE_REC_CASH_REBATE = "enable_rec_cash_rebate";
    //登录启用验证码
    public static final String LOGIN_SHOW_VERIFY_CODE = "login_show_verify_code";
    //开启APP登录优惠触发
    public static final String ENABLE_APP_LOGIN_PROMOTION = "enable_app_login_promotion";

    //注册显示手机
    public static final String REGISTER_SHOW_PHONE = "register_show_phone";
    //注册显示真名
    public static final String REGISTER_SHOW_REALNAME = "register_show_realname";
    //注册显示邮箱
    public static final String REGISTER_SHOW_EMAIL = "register_show_email";
    //注册显示昵称
    public static final String REGISTER_SHOW_NICKNAME = "register_show_nickname";
    //注册显示QQ
    public static final String REGISTER_SHOW_QQ = "register_show_qq";
    //注册显示微信
    public static final String REGISTER_SHOW_WEIXIN = "register_show_weixin";
    //注册显示短信验证码
    public static final String REGISTER_SHOW_SMS_CODE = "register_show_sms_code";
    //注册显示图形验证码
    public static final String REGISTER_SHOW_VERIFY_CODE = "register_show_verify_code";
    //注册真实姓名不能重复
    public static final String UNDUPLICATED_REAL_NAME = "unduplicated_real_name";
    //注册成功是否发送站内信
    public static final String FLAG_REGISTER_SEND_MSG = "flag_register_send_msg";
    //注册成功发送的站内信内容
    public static final String MSG_TEMPLATE = "msg_template";

    //试玩用户禁止第三方游戏
    public static final String TEST_USER_THIRD_DENY = "test_user_third_deny";
    //试玩用户禁止资金操作
    public static final String TEST_USER_FUND_DENY = "test_user_fund_deny";
    //试玩用户禁止彩票投注
    public static final String TEST_USER_BET_DENY = "test_user_bet_deny";

    /**
     * 存提款区块
     */

    //关闭提款申请稽核提示
    public static final String CLOSE_AUDIT = "close_audit";
    //线下支付只能一笔支付中
    public static final String OFFLINE_ONLY_ONE_PAYING = "offline_only_one_paying";
    //提款禁用风控初审
    public static final String DISABLE_RISK_AUDIT = "disable_risk_audit";
    //稽核放宽额度
    public static final String AUDIT_ALLOW = "audit_allow";
    //提款最低金额
    public static final String MIN_WITHDRAW_AMOUNT = "min_withdraw_amount";
    //提款投注与充值比例阈值
    public static final String WITHDRAW_THRESHOLD = "withdraw_threshold";
    //提款作业，后台操作人能否锁定
    public static final String WITHDRAW_ENABLE_LOCKED = "withdraw_enable_locked";

    //支付展开
    public static final String PAY_EXTEND = "pay_extend";

    //使用指定支付模式
    public static final String ONE_THIRD_PAY_MODE = "one_third_pay_mode";

    //支付当天请求
    public static final String TODAY_REQ = "today_req";
    //支付当天请求成功
    public static final String TODAY_RESP = "today_resp";
    //支付当天支付成功
    public static final String TODAY_PAY_SUCCESS = "today_pay_success";
    //返还手续费描述
    public static final String RETURN_DESC = "return_desc";

    /**
     * 代理区块
     */

    //代理开启返水设置
    public static final String AGENT_CASH_REBATE_ENABLE = "agent_cash_rebate_enable";
    //开启代理层级佣金设置
    public static final String AGENT_COMMISSION_ENABLE = "agent_commission_enable";
    //代理最大层级
    public static final String MAX_AGENT_LEVEL = "max_agent_level";
    //总代理展示所有下级代理信息
    public static final String AGENT_SHOW_ALL_SUB_CHANNEL = "agent_show_all_sub_channel";
    //允许代理查看下层的层级设置
    public static final String AGENT_SHOW_ALL_SUB_CHANNEL_LEVEL = "agent_show_all_sub_channel_level";
    //代理的红包功能
    public static final String AGENT_RED_PACKET = "agent_red_packet";
    //代理后台是否显示会员资料
    public static final String AGENT_SHOW_CONTACT = "agent_show_contact";
    //代理是否有推荐码
    public static final String AGENT_RECOMMEND_ENABLE = "agent_recommend_enable";

    /**
     * SMS
     */

    //短信用户ID
    public static final String SMS_USER_ID = "sms_user_id";
    //短信用户密码
    public static final String SMS_USER_PASSWORD = "sms_user_password";
    //短信接口地址
    public static final String SMS_API = "sms_api";
    //备用国际短信接口地址
    public static final String SMS_SPARE_FOREIGN_API = "sms_spare_foreign_api";
    //备用国际短信用户ID
    public static final String SMS_SPARE_FOREIGN_USER_ID = "sms_spare_foreign_user_id";
    //备用国际短信密码
    public static final String SMS_SPARE_FOREIGN_USER_PASSWORD = "sms_spare_foreign_user_password";
    //备用短信接口地址
    public static final String SMS_SPARE_API = "sms_spare_api";
    //备用短信用户ID
    public static final String SMS_SPARE_USER_ID = "sms_spare_user_id";
    //备用短信密码
    public static final String SMS_SPARE_USER_PASSWORD = "sms_spare_user_password";
    //备用短信签名
    public static final String SMS_SPARE_SIGN = "sms_spare_sign";
    //短信接口
    public static final String SMS_INTERFACE = "sms_interface";
    //短信参数
    public static final String SMS_PARAMS = "sms_params";

    /**
     * 红包部份
     */

    //发红包校验密码
    public static final String SEND_PACKET_CHECK_PASSWORD = "send_packet_check_password";
    //红包功能
    public static final String RED_PACKET = "red_packet";
    //最大中雷金额
    public static final String RED_PACKET_MAX_AMOUNT = "red_packet_max_amount";
    //禁抢中抢包人昵称
    public static final String RED_PACKET_SEND_GRAB_NAME = "red_packet_send_grab_name";
    //禁抢中抢包人人数
    public static final String RED_PACKET_SEND_GRAB_SIZE = "red_packet_send_grab_size";
    //福利发包人昵称
    public static final String RED_PACKET_FULI_NAME = "red_packet_fuli_name";
    //红包使用，提款必须为100的整数倍
    public static final String WITHDRAW_FULL_100 = "withdraw_full_100";
    //扫雷中免死是否第一个抢包
    public static final String RED_PACKET_SYSTEM_FIRST_GRAB = "red_packet_system_first_grab";
    //红包返水是否按房间反水
    public static final String RED_GAME_BASE = "red_game_base";
    //开放红包接口登录地址
    public static final String THIRD_RED_LOGIN_URL = "third_red_login_url";
    //开放红包接口登录地址(到房间)
    public static final String THIRD_RED_LOGIN_TO_ROOM_URL = "third_red_login_to_room_url";
    //红包开启维护
    public static final String RED_MAINTAIN = "red_maintain";


    /**
     * 其他部分
     */

    //Redis中用户第三方余额key的前缀
    public static final String THIRD_BALANCE_PREFIX = "third_balance_";
    //Redis中第三方平台最后同步时间
    public static final String SYNC_THIRD_PLATFORM_T = "sync_third_platform_t";
    //提示声重复
    public static final String NOTICE_VOICE_REPEAT = "notice_voice_repeat";

    /**
     * 彩票部分
     */
    // 启用彩票点杀 控制功能
    public static final String ENABLE_SELF_LOTTO_CTL = "enable_self_lotto_ctl";

    /**
     * 多语言
     */
    //前端语言
    public static final String LANG = "lang";
    //国家
    public static final String COUNTRY = "country";
    //中国语言代码
    public static final String ZH_CN = "zh-CN";
    //印度
    public static final String IND = "ind";
}
