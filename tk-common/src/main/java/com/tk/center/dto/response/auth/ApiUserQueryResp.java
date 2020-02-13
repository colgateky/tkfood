package com.tk.center.dto.response.auth;

import com.tk.center.dto.response.ApiResp;
import com.magic.springboot.entity.User;
import org.springframework.data.domain.Page;

/**
 * Created by xm on 2017/5/24.
 */
public class ApiUserQueryResp extends ApiResp {
    protected Page<User> page;

    public Page<User> getPage() {
        return page;
    }

    public void setPage(Page<User> page) {
        this.page = page;
    }
}
