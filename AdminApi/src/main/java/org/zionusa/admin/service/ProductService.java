package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.ProductDao;
import org.zionusa.admin.domain.Product;
import org.zionusa.base.service.BaseService;

import java.util.List;

@Service
public class ProductService extends BaseService<Product, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(Product.class);
    private final ProductDao dao;


    public ProductService(ProductDao dao) {
        super(dao, logger, Product.class);
        this.dao = dao;
    }

    public List<Product> getAllProducts() {
        return  dao.findAll();
    }
}
