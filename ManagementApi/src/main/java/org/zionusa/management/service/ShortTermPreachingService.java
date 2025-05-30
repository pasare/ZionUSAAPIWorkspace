package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.management.dao.ShortTermPreachingDao;
import org.zionusa.management.domain.ShortTermPreaching;

import java.util.List;
import java.util.Map;

@Service
public class ShortTermPreachingService extends BaseService<ShortTermPreaching, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ShortTermPreachingService.class);
    private final ShortTermPreachingDao shortTermPreachingDao;

    @Autowired
    ShortTermPreachingService(ShortTermPreachingDao shortTermPreachingDao) {
        super(shortTermPreachingDao, logger, ShortTermPreaching.class);
        this.shortTermPreachingDao = shortTermPreachingDao;
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<ShortTermPreaching> getAll(Boolean archived) {
        return super.getAll(archived);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Page<ShortTermPreaching> getAllByPage(Pageable pageable) {
        return super.getAllByPage(pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        return super.getAllDisplay(columns, archived);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public ShortTermPreaching getById(Integer id) throws NotFoundException {
        return super.getById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('Admin')")
    public ShortTermPreaching patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public ShortTermPreaching save(ShortTermPreaching t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<ShortTermPreaching> saveMultiple(List<ShortTermPreaching> tList) {
        return super.saveMultiple(tList);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('Admin')")
    public void delete(Integer id) throws NotFoundException {
        super.delete(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteMultiple(String ids) throws NotFoundException {
        super.deleteMultiple(ids);
    }

}
