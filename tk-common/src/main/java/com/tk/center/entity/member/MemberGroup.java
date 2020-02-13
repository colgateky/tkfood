package com.tk.center.entity.member;

import com.tk.center.entity.BaseModel;

/**
 * Created by xm on 2017/8/8.
 */
public class MemberGroup extends BaseModel {
    protected String name;
    protected int order;
    protected String className;
    protected boolean defaultGroup;
    protected int level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(boolean defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
