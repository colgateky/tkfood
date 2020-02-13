package com.tk.center.dto.response.auth;

import com.tk.center.dto.response.ApiResp;
import com.magic.springboot.entity.Role;

import java.util.List;

/**
 * Created by ifuck on 2017/5/26.
 */
public class ApiAllRolesResp extends ApiResp {
    protected List<Role> list;

    public List<Role> getList() {
        return list;
    }

    public void setList(List<Role> list) {
        this.list = list;
    }
}
