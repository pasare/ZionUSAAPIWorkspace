package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.ProductDao;
import org.zionusa.admin.dao.ProductDetailsDao;
import org.zionusa.admin.domain.Product;
import org.zionusa.base.service.BaseService;

import java.util.List;

@Service
public class ProductDetailsService extends BaseService<Product.ProductDetails, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(Product.ProductDetails.class);
    private final ProductDetailsDao dao;


    public ProductDetailsService(ProductDetailsDao dao) {
        super(dao, logger, Product.ProductDetails.class);
        this.dao = dao;
    }

    public List<Product.ProductDetails> getAllProductDetails() {
        return  dao.findAll();
    }
}
