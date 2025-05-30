package org.zionusa.management.domain.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;

@Service
public class AccessService extends BaseService<Access, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AccessService.class);

    @Autowired
    AccessService(AccessDao dao) {
        super(dao, logger, Access.class);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Access patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Access save(Access item) {
        return super.save(item);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<Access> saveMultiple(List<Access> tList) {
        return super.saveMultiple(tList);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void delete(Integer id) throws NotFoundException {
        super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteMultiple(String ids) throws NotFoundException {
        super.deleteMultiple(ids);
    }
}
