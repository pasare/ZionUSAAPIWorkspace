package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.Product;
import org.zionusa.admin.service.ProductConditionService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/product-conditions")
public class ProductConditionController extends BaseController<Product.Condition, Integer> {
    ProductConditionService service;

    @Autowired
    public ProductConditionController(ProductConditionService service) {
        super(service);
    }
}
