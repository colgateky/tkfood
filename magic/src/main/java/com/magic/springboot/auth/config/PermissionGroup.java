package com.magic.springboot.auth.config;

import java.util.List;

/**
 * Created by xm on 2017/5/19.
 */
public class PermissionGroup {
    protected String name;
    protected List<PermissionGroup> groups;
    protected List<Permission> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<PermissionGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<PermissionGroup> groups) {
        this.groups = groups;
    }
}
