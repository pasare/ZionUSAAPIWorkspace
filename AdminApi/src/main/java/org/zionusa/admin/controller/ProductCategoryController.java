package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.Product;
import org.zionusa.admin.service.ProductCategoryService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/product-categories")
public class ProductCategoryController extends BaseController<Product.Category, Integer> {
    ProductCategoryService service;

    @Autowired
    public ProductCategoryController(ProductCategoryService service) {
        super(service);
    }
}
