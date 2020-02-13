package com.tk.center.dto.response.auth;

import com.tk.center.dto.response.ApiResp;

import java.util.Set;

/**
 * Created by xm on 2017/5/19.
 */
public class ApiUserInfoResp extends ApiResp {
    protected String userId;
    protected String username;
    protected boolean root;
    protected Set<String> resources;
    protected Set<String> roleNames;

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getResources() {
        return resources;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources;
    }

    public Set<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(Set<String> roleNames) {
        this.roleNames = roleNames;
    }
}
