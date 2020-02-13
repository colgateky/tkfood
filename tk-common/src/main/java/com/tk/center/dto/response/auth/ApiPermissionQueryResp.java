package com.tk.center.dto.response.auth;

import com.tk.center.dto.response.ApiResp;
import com.magic.springboot.auth.config.PermissionGroup;

import java.util.List;

/**
 * Created by ifuck on 2017/5/26.
 */
public class ApiPermissionQueryResp extends ApiResp {
    public List<PermissionGroup> getPgs() {
        return pgs;
    }

    public void setPgs(List<PermissionGroup> pgs) {
        this.pgs = pgs;
    }

    protected List<PermissionGroup> pgs;
}
