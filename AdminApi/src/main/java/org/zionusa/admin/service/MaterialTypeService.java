package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.MaterialTypeDao;
import org.zionusa.admin.domain.MaterialType;
import org.zionusa.base.service.BaseService;

@Service
public class MaterialTypeService extends BaseService<MaterialType, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(MaterialTypeService.class);

    @Autowired
    public MaterialTypeService(MaterialTypeDao materialTypeDao) {super(materialTypeDao, logger, MaterialType.class);}

}
