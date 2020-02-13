package com.tk.center.entity.Merchandise;

import com.tk.center.entity.BaseModel;

/**
 * Created by Mingkun on 2020/02/07.
 */
public class ProductCategory extends BaseModel {
    protected String name;
    protected int order;

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
}
