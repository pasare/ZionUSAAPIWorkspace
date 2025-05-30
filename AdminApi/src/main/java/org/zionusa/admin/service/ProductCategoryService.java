package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.ProductCategoryDao;
import org.zionusa.admin.domain.Product;
import org.zionusa.base.service.BaseService;

import java.util.List;

@Service
public class ProductCategoryService extends BaseService<Product.Category, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(Product.Category.class);
    private final ProductCategoryDao dao;


    public ProductCategoryService(ProductCategoryDao dao) {
        super(dao, logger, Product.Category.class);
        this.dao = dao;
    }

    public List<Product.Category> getAllProductCategories() {
        return  dao.findAll();
    }
}
