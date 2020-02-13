package com.tk.center.service;

import com.magic.springboot.dao.DaoHelper;
import com.tk.center.dto.product.ProductQuery;
import com.tk.center.entity.Merchandise.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Mingkun on 2020/02/07.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    protected DaoHelper daoHelper;

    @Override
    public Page<Product> getMerchandriseWithPage(ProductQuery query) {
        return daoHelper.getPage(query, Product.class);
    }
}
