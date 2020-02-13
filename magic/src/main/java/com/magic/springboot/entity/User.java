package com.magic.springboot.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Set;

/**
 * Created by xm on 2017/5/19.
 */
public class User {
    @Id
    protected String id;
    protected String username;
    protected String password;
    protected String realName;
    protected boolean root;
    protected Date created;

    protected Set<String> roleIds;

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    protected Set<String> permissionIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getCreated() {
        return created;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<String> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(Set<String> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
