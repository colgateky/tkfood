package com.tk.center.dto.response;

import java.util.Collection;

/**
 * Created by xm on 2017/8/8.
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
