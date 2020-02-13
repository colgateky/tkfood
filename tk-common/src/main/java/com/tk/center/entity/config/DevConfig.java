package com.tk.center.entity.config;

import com.tk.center.entity.BaseModel;

/**
 * Created by Mingkun on 2020/02/04.
 */
public class DevConfig extends BaseModel {
    protected String name;
    protected String value;
    protected String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
