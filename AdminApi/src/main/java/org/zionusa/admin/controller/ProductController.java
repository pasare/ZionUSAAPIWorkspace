package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.Product;
import org.zionusa.admin.service.ProductService;
import org.zionusa.base.controller.BaseController;


@RestController
@RequestMapping("/products")
public class ProductController extends BaseController<Product, Integer> {
    ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        super(service);
    }
}


