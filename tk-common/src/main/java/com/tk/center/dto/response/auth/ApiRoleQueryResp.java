package com.tk.center.dto.response.auth;

import com.tk.center.dto.response.ApiResp;
import com.magic.springboot.entity.Role;
import org.springframework.data.domain.Page;

/**
 * Created by ifuck on 2017/5/26.
 */
public class ApiRoleQueryResp extends ApiResp {
    public Page<Role> getPage() {
        return page;
    }

    public void setPage(Page<Role> page) {
        this.page = page;
    }

    protected Page<Role> page;
}
