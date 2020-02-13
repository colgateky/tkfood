package com.tk.center.admin.dto;

import java.util.Map;

/**
 * Created by Mingkun on 2020/02/06.
 */
public class ApiStatusCountResp extends ApiResp {
    protected Map<String, Long> counts;

    public Map<String, Long> getCounts() {
        return counts;
    }

    public void setCounts(Map<String, Long> counts) {
        this.counts = counts;
    }
}
