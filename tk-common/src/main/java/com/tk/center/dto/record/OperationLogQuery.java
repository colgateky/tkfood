package com.tk.center.dto.record;

import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class OperationLogQuery extends TimeScopePageQuery {
    protected String relatedId;
    protected String modelCls;
    protected String userId;
    protected String ip;
    protected String description;

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public String getModelCls() {
        return modelCls;
    }

    public void setModelCls(String modelCls) {
        this.modelCls = modelCls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        this.buildLike(c, "relatedId", relatedId);
        if (StringUtils.isEmpty(modelCls) == false) {
            c.and("clsName").is(modelCls);
        }
        if (StringUtils.isEmpty(userId) == false) {
            c.and("userId").is(userId);
        }
        if (StringUtils.isEmpty(ip) == false) {
            c.and("ip").is(ip);
        }
        if (StringUtils.isEmpty(description) == false) {
            c.and("description").regex(description);
        }
    }

    @Override
    public void buildSortObject(DBObject sort) {
        sort.put("created", -1);
    }
}
