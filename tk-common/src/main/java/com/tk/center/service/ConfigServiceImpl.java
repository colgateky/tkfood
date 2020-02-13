package com.tk.center.service;

import com.magic.springboot.dao.DaoHelper;
import com.tk.center.entity.config.DevConfig;
import com.tk.center.entity.config.SysConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created by Mingkun on 2020/02/04.
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    @Resource
    protected DaoHelper daoHelper;

    @Override
    public boolean getSysConfigAsBoolean(String id) {
        String v = getSysConfigAsString(id);
        if (StringUtils.isEmpty(v)) {
            return false;
        }
        return Boolean.valueOf(v.trim());
    }

    @Override
    public String getSysConfigAsString(String id) {
        SysConfig config = getSysConfig(id);
        if (config == null) {
            return null;
        }
        return config.getValue();
    }

    @Override
    public BigDecimal getSysConfigAsBigDecimal(String id) {
        String v = getSysConfigAsString(id);
        if (StringUtils.isEmpty(v)) {
            return new BigDecimal(0);
        }
        return new BigDecimal(v.trim());
    }

    @Override
    public int getSysConfigAsInt(String id) {
        String v = getSysConfigAsString(id);
        if (StringUtils.isEmpty(v)) {
            return 0;
        }
        return Integer.valueOf(v.trim());
    }

    @Override
    public boolean getSysConfigAsBoolean(String id, String siteId) {
        String v = getSysConfigAsString(id, siteId);
        if (StringUtils.isEmpty(v)) {
            return false;
        }
        return Boolean.valueOf(v.trim());
    }

    @Override
    public String getSysConfigAsString(String id, String siteId) {
        SysConfig config = getSysConfig(id, siteId);
        if (config == null) {
            return null;
        }
        return config.getValue();
    }

    @Override
    public BigDecimal getSysConfigAsBigDecimal(String id, String siteId) {
        String v = getSysConfigAsString(id, siteId);
        if (StringUtils.isEmpty(v)) {
            return new BigDecimal(0);
        }
        return new BigDecimal(v.trim());
    }

    @Override
    public int getSysConfigAsInt(String id, String siteId) {
        String v = getSysConfigAsString(id, siteId);
        if (StringUtils.isEmpty(v)) {
            return 0;
        }
        return Integer.valueOf(v.trim());
    }

    @Override
    public SysConfig getSysConfig(String id) {
        return daoHelper.findById(id, SysConfig.class);
    }

    @Override
    public SysConfig getSysConfig(String id, String siteId) {
        SysConfig ret = this.getSysConfig(id);
        if (ret != null && ret.getValueMap() != null && !StringUtils.isEmpty(siteId)) {
            String value = ret.getValueMap().get(siteId);
            if (!StringUtils.isEmpty(value)) {
                ret.setValue(value);
            }
        }
        return ret;
    }

    @Override
    public boolean getDevConfigAsBoolean(String id) {
        String v = getDevConfigAsString(id);
        if (StringUtils.isEmpty(v)) {
            return false;
        }
        return Boolean.valueOf(v.trim());
    }

    @Override
    public String getDevConfigAsString(String id) {
        DevConfig config = getDevConfig(id);
        if (config == null) {
            return null;
        }
        return config.getValue();
    }

    @Override
    public BigDecimal getDevConfigAsBigDecimal(String id) {
        String v = getDevConfigAsString(id);
        if (StringUtils.isEmpty(v)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(v.trim());
    }

    @Override
    public int getDevConfigAsInt(String id) {
        String v = getDevConfigAsString(id);
        if (StringUtils.isEmpty(v)) {
            return 0;
        }
        return Integer.valueOf(v.trim());
    }

    private DevConfig getDevConfig(String id) {
        return daoHelper.findById(id, DevConfig.class);
    }
}
