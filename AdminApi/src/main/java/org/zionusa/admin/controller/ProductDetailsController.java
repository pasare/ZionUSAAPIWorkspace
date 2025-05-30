package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.Product;
import org.zionusa.admin.service.ProductDetailsService;
import org.zionusa.admin.service.ProductService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/product-details")
public class ProductDetailsController extends BaseController<Product.ProductDetails, Integer> {
    ProductDetailsService service;

    @Autowired
    public ProductDetailsController(ProductDetailsService service) {
        super(service);
    }
}
