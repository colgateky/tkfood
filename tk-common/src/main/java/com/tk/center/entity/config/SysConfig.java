package com.tk.center.entity.config;

import com.tk.center.entity.BaseModel;

import java.util.Map;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class SysConfig extends BaseModel {
    public static final String ThirdPayReturn = "third_pay_return";
    public static final String OfflinePayReturn = "offline_pay_return";
    public static final String MaxPayReturn = "max_pay_return";
    public static final String DayOfflineMaxPayReturn = "day_offline_max_pay_return";
    public static final String DayWithdrawLimit = "day_withdraw_limit"; //单日提款次数限制
    /**
     * 启用常态稽核
     */
    public static final String EnableNormalAudit = "enable_normal_audit";
    /**
     * 稽核倍数
     */
    public static final String NormalAuditTimes = "normal_audit_times";
    /**
     * 常态稽核放宽额度
     */
    public static final String NormalAuditAllow = "normal_audit_allow";

    /**
     * 返还手续费稽核配置
     */
    public static final String EnableReturnAudit = "enable_return_audit";
    public static final String ReturnAuditTimes = "return_audit_times";

    public static final String[] keys = new String[]{
            ThirdPayReturn, OfflinePayReturn, MaxPayReturn,
            EnableNormalAudit, NormalAuditTimes, NormalAuditAllow,
            EnableReturnAudit, ReturnAuditTimes
    };

    protected String name;
    protected String value;
    protected boolean visible;    // 前端API请求是否可见
    protected String type;
    protected String categoryId;
    protected int order;
    protected boolean open;  // 开放给用户配置
    protected Map<String, String> valueMap;
    protected String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Map<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, String> valueMap) {
        this.valueMap = valueMap;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
