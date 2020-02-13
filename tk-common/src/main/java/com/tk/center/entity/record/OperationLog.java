package com.tk.center.entity.record;

import com.magic.utils.JsonUtils;
import com.magic.utils.UUIDGenerator;
import com.tk.center.entity.BaseModel;
import org.springframework.data.annotation.Transient;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Mingkun on 2020/02/03.
 */
public class OperationLog extends BaseModel {
    protected String userId;
    protected String clsName;
    protected String relatedId;
    protected String oldStatus;
    protected String newStatus;
    protected String description;
    protected String message;
    @Transient
    protected String userName;
    protected String ip;

    public static OperationLog create(String userId, String relatedId, String oldStatus,
                                      String newStatus, String description, String message,
                                      String ip,
                                      Class cls) {
        OperationLog log = new OperationLog();
        log.setId(UUIDGenerator.generate());
        log.setCreated(new Date());
        log.setUserId(userId);
        log.setRelatedId(relatedId);

        //判断是否为json字串
        if (JsonUtils.isJsonValid(oldStatus) && JsonUtils.isJsonValid(newStatus)) {
            Map<String, String> oldMap = JsonUtils.parseJson(oldStatus, Map.class);
            Map<String, String> newMap = JsonUtils.parseJson(newStatus, Map.class);
            Map<String, Object> oldMap2 = new LinkedHashMap<>();
            Map<String, Object> newMap2 = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : oldMap.entrySet()) {
                if (newMap.containsValue(entry.getValue())) {
                    continue;
                }
                oldMap2.put(entry.getKey(), entry.getValue());
                newMap2.put(entry.getKey(), newMap.get(entry.getKey()));
            }
            //资料没有改动
            if (oldMap2.size() == 0 || (oldMap2.size() == 1 && !StringUtils.isEmpty(oldMap2.get("updated")))) {
                return new OperationLog();
            }
            log.setOldStatus(JsonUtils.toJson(oldMap2));
            log.setNewStatus(JsonUtils.toJson(newMap2));
        } else {
            log.setOldStatus(oldStatus);
            log.setNewStatus(newStatus);
        }
        log.setDescription(description);
        log.setMessage(message);
        log.setIp(ip);
        log.setClsName(cls.getSimpleName());
        return log;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
