package com.tk.center.common;

import com.magic.springboot.client.Client;
import com.magic.springboot.config.MultiConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Mingkun on 2020/02/07.
 */
@Component
@ConfigurationProperties(prefix="sconfig")
public class ServerConfig {

    @Resource
    protected Client client;

    private boolean dev;
    private boolean needAuth;
    protected String clientId;
    protected boolean useHttps = true;
    protected String uploadUrl;

    public boolean isDev() {
        return dev;
    }

    public void setDev(boolean dev) {
        this.dev = dev;
    }

    public boolean isNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(boolean needAuth) {
        this.needAuth = needAuth;
    }

    public String getClientId() {
        if (MultiConfig.enableMulti && MultiConfig.MultiListener != null){
            return MultiConfig.MultiListener.getGameClientId(client.getClientId());
        }
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isUseHttps() {
        return useHttps;
    }

    public void setUseHttps(boolean useHttps) {
        this.useHttps = useHttps;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
}
