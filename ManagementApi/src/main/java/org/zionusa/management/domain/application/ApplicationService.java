package org.zionusa.management.domain.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.exception.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ApplicationService extends BaseService<Application, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    private final ApplicationDao applicationDao;

    @Autowired
    ApplicationService(ApplicationDao applicationDao) {
        super(applicationDao, logger, Application.class);
        this.applicationDao = applicationDao;
    }

    @PreAuthorize("hasAuthority('Admin')")
    public void enable(Integer id, Boolean enabled) throws NotFoundException {
        Optional<Application> applicationOptional = applicationDao.findById(id);

        if (!applicationOptional.isPresent()) {
            throw new NotFoundException("Application does not exist");
        }

        Application application = applicationOptional.get();
        application.setEnabled(enabled);
        application.setArchived(false);

        applicationDao.save(application);
    }

    // PreAuthorize(Public)
    public String getLatestVersion(String xApplicationVersion) throws NotFoundException {
        Optional<Application> optionalApplication = applicationDao.findByUniqueId(xApplicationVersion);

        if (!optionalApplication.isPresent()) {
            throw new NotFoundException("Unknown application");
        }

        return optionalApplication.get().getUniqueId();
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Application patchById(Integer id, Map<String, Object> fields) throws org.zionusa.base.util.exceptions.NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Application save(Application t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<Application> saveMultiple(List<Application> tList) {
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
