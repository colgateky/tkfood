package com.tk.center.dto.product;

import com.mongodb.DBObject;
import com.tk.center.dto.record.TimeScopePageQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * Created by Mingkun on 2020/02/07.
 */
public class ProductQuery extends TimeScopePageQuery {
    protected String productId;
    protected String productSn;
    protected String productName;
    protected boolean noSort;

    @Override
    public void buildCriteria(Criteria c) {
        super.buildCriteria(c);
        if (!StringUtils.isEmpty(productId)) {
            c.and("id").is(productId);
        }
        if (!StringUtils.isEmpty(productSn)) {
            c.and("productSn").is(productSn);
        }
        if (!StringUtils.isEmpty(productName)) {
            c.and("productName").regex(productName);
        }
    }

    @Override
    public void buildSortObject(DBObject sort) {
        super.buildSortObject(sort);
        if (!noSort) {
            sort.put("created", -1);
        }
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isNoSort() {
        return noSort;
    }

    public void setNoSort(boolean noSort) {
        this.noSort = noSort;
    }
}
