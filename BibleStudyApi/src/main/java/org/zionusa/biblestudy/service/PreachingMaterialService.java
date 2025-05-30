package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.biblestudy.dao.PreachingMaterialDao;
import org.zionusa.biblestudy.domain.PreachingMaterial;

import java.util.List;
import java.util.Map;

@Service
public class PreachingMaterialService extends BaseService<PreachingMaterial, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(PreachingMaterialService.class);

    @Autowired
    public PreachingMaterialService(
        PreachingMaterialDao preachingMaterialDao
    ) {
        super(preachingMaterialDao, logger, PreachingMaterial.class);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public PreachingMaterial patchById(Integer id, Map<String, Object> fields) throws org.zionusa.base.util.exceptions.NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public PreachingMaterial save(PreachingMaterial t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<PreachingMaterial> saveMultiple(List<PreachingMaterial> tList) {
        return super.saveMultiple(tList);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void delete(Integer id) throws org.zionusa.base.util.exceptions.NotFoundException {
        super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteMultiple(String ids) throws org.zionusa.base.util.exceptions.NotFoundException {
        super.deleteMultiple(ids);
    }

}
