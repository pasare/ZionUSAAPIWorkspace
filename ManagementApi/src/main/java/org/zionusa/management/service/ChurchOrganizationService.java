package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.dao.ChurchOrganizationDao;
import org.zionusa.management.domain.ChurchOrganization;
import org.zionusa.management.exception.NotFoundException;

import java.util.Optional;

@Deprecated
@Service
public class ChurchOrganizationService extends BaseService<ChurchOrganization, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ChurchOrganizationService.class);

    private final ChurchOrganizationDao churchOrganizationDao;

    @Autowired
    public ChurchOrganizationService(ChurchOrganizationDao churchOrganizationDao) {
        super(churchOrganizationDao, logger, ChurchOrganization.class);
        this.churchOrganizationDao = churchOrganizationDao;
    }

    public ChurchOrganization getChurchOrganizationByChurchId(Integer churchId) {

        Optional<ChurchOrganization> churchOrganizationOptional = churchOrganizationDao.getChurchOrganizationByChurchId(churchId);

        if (!churchOrganizationOptional.isPresent())
            throw new NotFoundException("No organizational information preset for the church ");

        return churchOrganizationOptional.get();
    }

    @PreAuthorize("@authenticatedUserService.canModifyChurchOrganization(principal, #churchOrganization)")
    public ChurchOrganization save(ChurchOrganization churchOrganization) {
        //each church should only have the latest organization data saved

        Optional<ChurchOrganization> savedOrganizationOptional = churchOrganizationDao.getChurchOrganizationByChurchId(churchOrganization.getChurchId());
        savedOrganizationOptional.ifPresent(churchOrganization1 -> churchOrganization.setId(churchOrganization1.getId()));

        return churchOrganizationDao.save(churchOrganization);
    }

    @PreAuthorize("@authenticatedUserService.canDeleteChurchOrganization(principal, #id)")
    public void delete(Integer id) {
        Optional<ChurchOrganization> churchOrganizationOptional = churchOrganizationDao.getChurchOrganizationByChurchId(id);

        if (!churchOrganizationOptional.isPresent())
            throw new NotFoundException("The church organization could not be found");

        churchOrganizationDao.delete(churchOrganizationOptional.get());

    }

}
