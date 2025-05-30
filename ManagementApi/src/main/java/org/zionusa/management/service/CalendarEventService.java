package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.domain.UserPermission;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.management.dao.CalendarEventDao;
import org.zionusa.management.domain.CalendarEvent;
import org.zionusa.management.domain.CalendarEventCreateRequest;

import java.util.List;
import java.util.Map;

@Service
public class CalendarEventService extends BaseService<CalendarEvent, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(CalendarEventService.class);
    private final MicrosoftGraphService microsoftGraphService;

    public CalendarEventService(CalendarEventDao calendarEventDao, MicrosoftGraphService microsoftGraphService) {
        super(calendarEventDao, logger, CalendarEvent.class);
        this.microsoftGraphService = microsoftGraphService;
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public CalendarEvent createOneForOffice365(CalendarEventCreateRequest calendarEventCreateRequest) {
        String eventId = microsoftGraphService.createCalendarEvent(calendarEventCreateRequest);

        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setBranchId(calendarEventCreateRequest.getBranchId());
        calendarEvent.setEventId(eventId);

        return super.save(calendarEvent);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<CalendarEvent> getAll(Boolean archived) {
        return super.getAll(archived);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public Page<CalendarEvent> getAllByPage(Pageable pageable) {
        return super.getAllByPage(pageable);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        return super.getAllDisplay(columns, archived);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public CalendarEvent getById(Integer id) throws NotFoundException {
        return super.getById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public CalendarEvent patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public CalendarEvent save(CalendarEvent calendarEvent) {
        return super.save(calendarEvent);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public List<CalendarEvent> saveMultiple(List<CalendarEvent> calendarEvents) {
        return super.saveMultiple(calendarEvents);
    }

    @Override
    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public void delete(Integer id) throws NotFoundException {
        CalendarEvent calendarEvent = super.getById(id);

        // Delete from O365 calendar
        microsoftGraphService.deleteCalendarEvent(calendarEvent.getEventId());

        // Delete from DB
        super.delete(id);
    }

    @Override
    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public void deleteMultiple(String ids) throws NotFoundException {
        if (ids != null && !ids.isEmpty()) {
            String[] parsedIds = ids.split(",");

            for (String stringId : parsedIds) {
                Integer id = Integer.parseInt(stringId);
                delete(id);
            }
        }
    }

}
