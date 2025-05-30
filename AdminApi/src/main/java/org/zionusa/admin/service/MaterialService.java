package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.MaterialDao;
import org.zionusa.admin.domain.Material;
import org.zionusa.base.service.BaseService;

@Service
public class MaterialService extends BaseService<Material, Integer> {

        private static final Logger logger = LoggerFactory.getLogger(MaterialService.class);

        @Autowired
        public MaterialService(MaterialDao materialDao) {super(materialDao, logger, Material.class);}

}
