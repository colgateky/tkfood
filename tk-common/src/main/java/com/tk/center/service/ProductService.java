package com.tk.center.service;

import com.tk.center.dto.product.ProductQuery;
import com.tk.center.entity.Merchandise.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<Product> getMerchandriseWithPage(ProductQuery query);
}
