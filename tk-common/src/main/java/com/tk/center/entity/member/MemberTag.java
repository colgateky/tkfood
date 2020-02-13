package com.tk.center.entity.member;

import com.tk.center.entity.BaseModel;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class MemberTag extends BaseModel {
    protected String name;
    protected int order;
    protected String className;

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
}
