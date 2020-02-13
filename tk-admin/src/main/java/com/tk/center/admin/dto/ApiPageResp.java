package com.tk.center.admin.dto;

import org.springframework.data.domain.Page;

public class ApiPageResp extends ApiResp {
    protected Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

}
