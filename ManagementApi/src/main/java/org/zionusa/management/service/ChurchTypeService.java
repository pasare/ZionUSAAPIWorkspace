package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.dao.ChurchTypeDao;
import org.zionusa.management.domain.ChurchType;
import org.zionusa.management.exception.NotFoundException;

import java.util.Optional;

@Deprecated
@Service
public class ChurchTypeService extends BaseService<ChurchType, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ChurchTypeService.class);
    private final ChurchTypeDao churchTypeDao;

    @Autowired
    ChurchTypeService(ChurchTypeDao churchTypeDao) {
        super(churchTypeDao, logger, ChurchType.class);
        this.churchTypeDao = churchTypeDao;
    }

    @PreAuthorize("hasAuthority('Admin')")
    public ChurchType save(ChurchType churchType) {
        return churchTypeDao.save(churchType);
    }

    @PreAuthorize("hasAuthority('Admin')")
    public void delete(Integer id) {
        Optional<ChurchType> churchTypeOptional = churchTypeDao.findById(id);

        if (!churchTypeOptional.isPresent())
            throw new NotFoundException("The church type was not found");
    }

}
