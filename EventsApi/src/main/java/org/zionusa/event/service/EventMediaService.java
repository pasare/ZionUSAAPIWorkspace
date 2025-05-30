package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.domain.UserPermission;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.event.dao.EventMediaDao;
import org.zionusa.event.domain.EventMedia;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EventMediaService extends BaseService<EventMedia, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventMediaService.class);
    private final EventMediaDao eventMediaDao;

    @Autowired
    public EventMediaService(EventMediaDao eventMediaDao) {
        super(eventMediaDao, logger, EventMedia.class);
        this.eventMediaDao = eventMediaDao;
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<EventMedia> findAllByEventProposalId(Integer eventProposalId) {
        return eventMediaDao.findAllByEventProposalIdAndArchived(eventProposalId, false);
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<EventMedia> findAllByResultsSurveyId(Integer resultsSurveyId) {
        return eventMediaDao.findAllByResultsSurveyIdAndArchived(resultsSurveyId, false);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<EventMedia> getAll(Boolean archived) {
        return super.getAll(archived);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public Page<EventMedia> getAllByPage(Pageable pageable) {
        return super.getAllByPage(pageable);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        return super.getAllDisplay(columns, archived);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public EventMedia getById(Integer id) throws NotFoundException {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_MEDIA + "')")
    public EventMedia patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_MEDIA + "')")
    public EventMedia save(EventMedia t) {
        // Automatically set the updated date for event media
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate localDateTime = LocalDate.now(EZoneId.NEW_YORK.getValue());
        Date date = Date.from(localDateTime.atStartOfDay(EZoneId.NEW_YORK.getValue()).toInstant());
        t.setUpdatedDate(dateFormat.format(date));
        return super.save(t);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_MEDIA + "')")
    public List<EventMedia> saveMultiple(List<EventMedia> tList) {
        // Automatically set the updated date for event media
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate localDateTime = LocalDate.now(EZoneId.NEW_YORK.getValue());
        Date date = Date.from(localDateTime.atStartOfDay(EZoneId.NEW_YORK.getValue()).toInstant());
        tList.forEach(t -> t.setUpdatedDate(dateFormat.format(date)));
        return super.saveMultiple(tList);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_MEDIA + "')")
    public void delete(Integer id) throws NotFoundException {
        super.delete(id);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_MEDIA + "')")
    public void deleteMultiple(String ids) throws NotFoundException {
        super.deleteMultiple(ids);
    }
}
