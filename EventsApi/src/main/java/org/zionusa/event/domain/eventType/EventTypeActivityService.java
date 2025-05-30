package org.zionusa.event.domain.eventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.domain.UserPermission;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;

@Service
public class EventTypeActivityService extends BaseService<EventTypeActivity, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventTypeActivityService.class);

    @Autowired
    public EventTypeActivityService(EventTypeActivitiesDao EventTypeActivitiesDao) {
        super(EventTypeActivitiesDao, logger, EventTypeActivity.class);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_EVENT_TYPES + "')")
    public List<EventTypeActivity> getAll(Boolean archived) {
        return super.getAll(archived);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_EVENT_TYPES + "')")
    public Page<EventTypeActivity> getAllByPage(Pageable pageable) {
        return super.getAllByPage(pageable);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_EVENT_TYPES + "')")
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        return super.getAllDisplay(columns, archived);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_EVENT_TYPES + "')")
    public EventTypeActivity getById(Integer id) throws NotFoundException {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_TYPES + "')")
    public EventTypeActivity patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_TYPES + "')")
    public EventTypeActivity save(EventTypeActivity t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_TYPES + "')")
    public List<EventTypeActivity> saveMultiple(List<EventTypeActivity> tList) {
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
