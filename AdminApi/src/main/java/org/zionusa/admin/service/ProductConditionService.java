package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.ProductConditionDao;
import org.zionusa.admin.domain.Product;
import org.zionusa.base.service.BaseService;

import java.util.List;

@Service
public class ProductConditionService extends BaseService<Product.Condition, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(Product.Condition.class);
    private final ProductConditionDao dao;


    public ProductConditionService(ProductConditionDao dao) {
        super(dao, logger, Product.Condition.class);
        this.dao = dao;
    }

    public List<Product.Condition> getAllProductConditions() {
        return  dao.findAll();
    }
}
