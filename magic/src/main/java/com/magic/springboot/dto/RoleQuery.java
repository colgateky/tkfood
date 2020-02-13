package com.magic.springboot.dto;

import java.util.Set;

/**
 * Created by xm on 2017/5/19.
 */
public class RoleQuery extends PageQuery {
    protected String name;
    protected Set<String> roleIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }
}
