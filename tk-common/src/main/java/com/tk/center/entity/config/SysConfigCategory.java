package com.tk.center.entity.config;

import com.tk.center.entity.BaseModel;

/**
 * Created by Mingkun on 2020/02/05.
 */
public class SysConfigCategory extends BaseModel {
    protected String name;
    protected int order;
    protected boolean shortCut;

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

    public boolean isShortCut() {
        return shortCut;
    }

    public void setShortCut(boolean shortCut) {
        this.shortCut = shortCut;
    }
}
