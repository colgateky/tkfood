package com.magic.springboot.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Set;

/**
 * Created by xm on 2017/5/19.
 */
public class Role {
    @Id
    protected String id;
    protected String name;
    protected Date created;

    protected Set<String> permissionIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<String> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(Set<String> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
