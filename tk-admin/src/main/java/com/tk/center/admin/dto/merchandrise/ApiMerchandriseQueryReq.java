package com.tk.center.admin.dto.merchandrise;

import com.tk.center.admin.dto.ApiAuthReq;
import com.tk.center.dto.product.ProductQuery;

/**
 * Created by Mingkun on 2020/02/07.
 */
public class ApiMerchandriseQueryReq extends ApiAuthReq {
    protected ProductQuery query;

    public ProductQuery getQuery() {
        return query;
    }

    public void setQuery(ProductQuery query) {
        this.query = query;
    }
}
