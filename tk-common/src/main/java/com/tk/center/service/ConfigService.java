package com.tk.center.service;

import com.tk.center.entity.config.SysConfig;

import java.math.BigDecimal;

public interface ConfigService {
    SysConfig getSysConfig(String id);

    SysConfig getSysConfig(String id, String siteId);

    boolean getSysConfigAsBoolean(String id);
    String getSysConfigAsString(String id);
    BigDecimal getSysConfigAsBigDecimal(String id);
    int getSysConfigAsInt(String id);

    boolean getSysConfigAsBoolean(String id, String siteId);
    String getSysConfigAsString(String id, String siteId);
    BigDecimal getSysConfigAsBigDecimal(String id, String siteId);
    int getSysConfigAsInt(String id, String siteId);

    boolean getDevConfigAsBoolean(String id);
    String getDevConfigAsString(String id);
    BigDecimal getDevConfigAsBigDecimal(String id);
    int getDevConfigAsInt(String id);
}
