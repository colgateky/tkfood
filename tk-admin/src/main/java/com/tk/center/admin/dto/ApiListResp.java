package com.tk.center.admin.dto;

import java.util.Collection;

/**
 * Created by ming on 2020/02/03
 */
public class ApiListResp extends ApiResp {

    protected Collection list;

    public Collection getList() {
        return list;
    }

    public void setList(Collection list) {
        this.list = list;
    }
}
