package com.tk.center.entity.global;

import com.tk.center.entity.BaseModel;

/**
 * Created by ifuck on 2018/8/21.
 */
public class ClientSite extends BaseModel {
    private String name;
    private int order;

    //后端配置
    private MongoDB mongoDB;
    private RedisDB redisDB;
    private String gameServer;
    private String gameClientId;
    private String taskInfo;

    //前端配置
    private String adminApi;
    private String feApi;
    private String agentApi;
    private String adminDomain;
    private String whiteList;
    private boolean maintenance;
    private String callbacks;
    private String cfEmail;
    private String cfAuthKey;
    private String webSocketApi;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    public void setMongoDB(MongoDB mongoDB) {
        this.mongoDB = mongoDB;
    }

    public RedisDB getRedisDB() {
        return redisDB;
    }

    public void setRedisDB(RedisDB redisDB) {
        this.redisDB = redisDB;
    }

    public String getGameServer() {
        return gameServer;
    }

    public void setGameServer(String gameServer) {
        this.gameServer = gameServer;
    }

    public String getGameClientId() {
        return gameClientId;
    }

    public void setGameClientId(String gameClientId) {
        this.gameClientId = gameClientId;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAdminApi() {
        return adminApi;
    }

    public void setAdminApi(String adminApi) {
        this.adminApi = adminApi;
    }

    public String getAdminDomain() {
        return adminDomain;
    }

    public void setAdminDomain(String adminDomain) {
        this.adminDomain = adminDomain;
    }

    public String getAgentApi() {
        return agentApi;
    }

    public void setAgentApi(String agentApi) {
        this.agentApi = agentApi;
    }

    public String getFeApi() {
        return feApi;
    }

    public void setFeApi(String feApi) {
        this.feApi = feApi;
    }

    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public String getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(String callbacks) {
        this.callbacks = callbacks;
    }

    public String getCfEmail() {
        return cfEmail;
    }

    public void setCfEmail(String cfEmail) {
        this.cfEmail = cfEmail;
    }

    public String getCfAuthKey() {
        return cfAuthKey;
    }

    public void setCfAuthKey(String cfAuthKey) {
        this.cfAuthKey = cfAuthKey;
    }

    public String getWebSocketApi() {
        return webSocketApi;
    }

    public void setWebSocketApi(String webSocketApi) {
        this.webSocketApi = webSocketApi;
    }
}
