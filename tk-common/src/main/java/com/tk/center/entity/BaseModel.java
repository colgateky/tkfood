package com.tk.center.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by hmgua on 2017/8/2.
 */
public class BaseModel {
    @Id
    protected String id;
    protected Date created;
    protected Date updated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
