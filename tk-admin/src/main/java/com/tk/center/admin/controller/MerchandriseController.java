package com.tk.center.admin.controller;

import com.tk.center.admin.dto.ApiPageResp;
import com.tk.center.admin.dto.merchandrise.ApiMerchandriseQueryReq;
import com.tk.center.dto.product.ProductQuery;
import com.tk.center.entity.Merchandise.Product;
import com.tk.center.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by Mingkun on 2020/02/07.
 */
public class MerchandriseController {

    @Resource
    protected ProductService productService;

    @RequestMapping("members.{ext}")
    public ApiPageResp getPage(@RequestBody ApiMerchandriseQueryReq req) {
        ProductQuery productQuery = req.getQuery();
        Page<Product> page = productService.getMerchandriseWithPage(productQuery);
        ApiPageResp ret = new ApiPageResp();
        ret.setPage(page);
        return ret;
    }
}
