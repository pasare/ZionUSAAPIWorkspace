package org.zionusa.event.domain.eventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
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
@CacheConfig(cacheNames = "event-types-cache")
public class EventTypesService extends BaseService<EventType, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventTypesService.class);

    @Autowired
    public EventTypesService(EventTypesDao eventTypesDao) {
        super(eventTypesDao, logger, EventType.class);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_EVENT_TYPES + "')")
    public List<EventType> getAll(Boolean archived) {
        return super.getAll(archived);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_EVENT_TYPES + "')")
    public Page<EventType> getAllByPage(Pageable pageable) {
        return super.getAllByPage(pageable);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_EVENT_TYPES + "')")
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        return super.getAllDisplay(columns, archived);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_EVENT_TYPES + "')")
    public EventType getById(Integer id) throws NotFoundException {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_TYPES + "')")
    public EventType patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_TYPES + "')")
    public EventType save(EventType t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_TYPES + "')")
    public List<EventType> saveMultiple(List<EventType> tList) {
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
