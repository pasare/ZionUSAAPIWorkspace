package org.zionusa.management.domain.title;

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
public class TitleService extends BaseService<Title, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(TitleService.class);

    @Autowired
    TitleService(TitleDao dao) {
        super(dao, logger, Title.class);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Title patchById(Integer id, Map<String, Object> fields) throws org.zionusa.base.util.exceptions.NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Title save(Title t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<Title> saveMultiple(List<Title> tList) {
        return super.saveMultiple(tList);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void delete(Integer id) throws org.zionusa.base.util.exceptions.NotFoundException {
        super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteMultiple(String ids) throws NotFoundException {
        super.deleteMultiple(ids);
    }
}
