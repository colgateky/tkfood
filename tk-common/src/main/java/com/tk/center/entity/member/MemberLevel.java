package com.tk.center.entity.member;

import com.tk.center.entity.BaseModel;

/**
 * Created by ifuck on 2018/4/5.
 */
public class MemberLevel extends BaseModel {
    protected String name;
    protected int level;
    protected String className;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
