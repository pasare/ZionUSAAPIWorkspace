package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.ForbiddenException;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.event.dao.EventFundraisingDao;
import org.zionusa.event.domain.EventFundraising;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventFundraisingService extends BaseService<EventFundraising, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventFundraisingService.class);
    private final EventFundraisingDao eventFundraisingDao;

    @Autowired
    public EventFundraisingService(EventFundraisingDao eventFundraisingDao) {
        super(eventFundraisingDao, logger, EventFundraising.class);
        this.eventFundraisingDao = eventFundraisingDao;
    }

    @Override
    public void delete(Integer id) throws ForbiddenException, NotFoundException {
        logger.warn("Attempting to delete all eventFundraisingId   {}", id);

        // Existing event fundraising
        if (id != null) {
            Optional<EventFundraising> optionalEventFundraising = eventFundraisingDao.findById(id);
            // Found in the database
            if (optionalEventFundraising.isPresent()) {
                if (canAccessEventFundraising(optionalEventFundraising.get())) {
                    super.delete(id);
                    return;
                }
                throw new ForbiddenException("Access Denied");
            }
        }

        throw new NotFoundException("Not Found");
    }

    @Override
    public List<EventFundraising> getAll(Boolean archived) throws ForbiddenException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.warn("{} (UserId: {}) Attempting to retrieve all event fundraising", authenticatedUser.getDisplayName(), authenticatedUser.getId());

        if (validateEventAdmin(authenticatedUser)) {
            logger.warn("Retrieving all event fundraising");
            return super.getAll(archived);
        }

        throw new ForbiddenException("Access denied");
    }

    public List<Map<String, Object>> getAllDisplayProtected(List<String> columns, Boolean archived) throws ForbiddenException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.warn("{} (UserId: {}) Attempting to retrieve all display event fundraising", authenticatedUser.getDisplayName(), authenticatedUser.getId());

        if (validateEventAdmin(authenticatedUser)) {
            logger.warn("Retrieving all display event fundraising");
            return getAllDisplay(columns, archived);
        }

        throw new ForbiddenException("Access denied");
    }

    public List<Map<String, Object>> getAllDisplayByEventId(Integer eventProposalId, List<String> columns) throws ForbiddenException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.warn("{} (UserId: {}) Attempting to retrieve all display event fundraising for eventProposalId: {}", authenticatedUser.getDisplayName(), authenticatedUser.getId(), eventProposalId);

        if (validateEventAdmin(authenticatedUser)) {
            logger.warn("Retrieving all display event fundraising for eventProposalId: {}", eventProposalId);
            return getAllDisplayFromList(eventFundraisingDao.getEventFundraisingsByEventId(eventProposalId), columns);
        }

        throw new ForbiddenException("Access denied");
    }

    public EventFundraising getByEventIdUserId(Integer eventProposalId, Integer userId) throws ForbiddenException, NotFoundException {
        logger.warn("Attempting to retrieve approved event fundraising page for eventProposalId: {}, fundraiserUserId: {}", eventProposalId, userId);
        EventFundraising eventFundraising = eventFundraisingDao.getEventFundraisingByEventIdAndUserId(eventProposalId, userId);

        if (eventFundraising != null) {
            // Return only approved events
            if (Boolean.TRUE.equals(eventFundraising.getApproved())) { // Public
                logger.warn("Retrieved public eventFundraisingId: {}", eventFundraising.getId());
                return eventFundraising;
            } else {
                AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (eventFundraising.getUserId().equals(authenticatedUser.getId())) {
                    logger.warn("Retrieved users own eventFundraisingId: {}", eventFundraising.getId());
                    return eventFundraising;
                } else if (validateEventAdmin(authenticatedUser)) {
                    logger.warn("Retrieved eventFundraisingId: {} via Event Admin permissions", eventFundraising.getId());
                    return eventFundraising;
                } else {
                    throw new ForbiddenException("Access denied");
                }
            }
        }

        throw new NotFoundException("Not found");
    }

    public List<Map<String, Object>> getAllDisplayByUserId(Integer userId, List<String> columns) throws ForbiddenException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.warn("{} (UserId: {}) Attempting to retrieve approved event fundraising page for userId: {}", authenticatedUser.getDisplayName(), authenticatedUser.getId(), userId);

        if (validateEventAdmin(authenticatedUser) || authenticatedUser.getId().equals(userId)) {
            logger.warn("Retrieving display event fundraising for userId: {}", userId);
            return getAllDisplayFromList(eventFundraisingDao.getEventFundraisingsByUserId(userId), columns);
        }

        throw new ForbiddenException("Access denied");
    }

    @Override
    public EventFundraising getById(Integer id) throws ForbiddenException, NotFoundException {
        logger.warn("Attempting to retrieve approved event fundraising id: {}", id);
        EventFundraising eventFundraising = super.getById(id);
        if (eventFundraising != null) {
            if (canAccessEventFundraising(eventFundraising)) {
                logger.warn("Retrieved event fundraising id: {}", id);
                return eventFundraising;
            }
            throw new ForbiddenException("Access denied");
        }
        throw new NotFoundException("Not found");
    }

    @Override
    public EventFundraising save(EventFundraising eventFundraising) throws ForbiddenException {
        // Existing event fundraising
        Integer eventFundraisingId = eventFundraising.getId();
        if (eventFundraisingId != null) {
            logger.warn("Attempting to save existing event fundraising");
            Optional<EventFundraising> optionalEventFundraising = eventFundraisingDao.findById(eventFundraisingId);
            // Found in the database
            if (optionalEventFundraising.isPresent()) {
                if (canAccessEventFundraising(optionalEventFundraising.get())) {
                    return super.save(eventFundraising);
                }
                throw new ForbiddenException("Access Denied");
            }
        }
        // New event fundraising
        logger.warn("Attempting to save new event fundraising");
        // Admins can save any event fundraising. Members can only save their own.
        if (canAccessEventFundraising(eventFundraising)) {
            return super.save(eventFundraising);
        }
        throw new ForbiddenException("Access Denied");
    }

    private boolean canAccessEventFundraising(EventFundraising eventFundraising) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.warn("{} (UserId {}) is attempting to access event fundraising: {}", authenticatedUser.getDisplayName(), authenticatedUser.getId(), eventFundraising.getId());

        if (validateEventAdmin(authenticatedUser) || authenticatedUser.getId().equals(eventFundraising.getUserId())) {
            logger.warn("{} (UserId {}) granted access", authenticatedUser.getDisplayName(), authenticatedUser.getId());
            return true;
        }

        logger.warn("{} (UserId {}) denied access", authenticatedUser.getDisplayName(), authenticatedUser.getId());
        return false;
    }

    private boolean validateEventAdmin(AuthenticatedUser authenticatedUser) {
        for (String applicationRole : authenticatedUser.getUserApplicationRoles()) {
            if (EApplicationRole.EVENT_DEFAULT_ADMIN.is(applicationRole) ||
                EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.is(applicationRole) ||
                EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.is(applicationRole))
                return true;
        }

        logger.warn("{} (UserId {}) does not have the role of EVENT_ADMIN", authenticatedUser.getDisplayName(), authenticatedUser.getId());
        return false;
    }
}
