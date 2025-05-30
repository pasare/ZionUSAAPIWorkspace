package org.zionusa.event.domain.eventProposal;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.zionusa.base.domain.UserPermission;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.enums.EUserRole;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.Util;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.ForbiddenException;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.event.authorizer.PermissionsAuthorizer;
import org.zionusa.event.dao.EventProposalTableViewDao;
import org.zionusa.event.dao.EventProposalVipDao;
import org.zionusa.event.dao.EventProposalVipViewDao;
import org.zionusa.event.domain.resultsSurvey.ResultsSurveysDao;
import org.zionusa.event.domain.*;
import org.zionusa.event.domain.eventPresenter.EventPresenterDao;
import org.zionusa.event.domain.eventPresenter.EventPresenterService;
import org.zionusa.event.domain.resultsSurvey.ResultsSurvey;
import org.zionusa.event.domain.resultsSurvey.ResultsSurveysService;
import org.zionusa.event.domain.eventCategory.EventCategory;
import org.zionusa.event.domain.eventCategory.EventCategoryDao;
import org.zionusa.event.domain.eventStatus.EventStatus;
import org.zionusa.event.domain.eventStatus.EventStatusService;
import org.zionusa.event.domain.eventTeam.EventTeamsService;
import org.zionusa.event.domain.eventvolunteer.EventVolunteerManagement;
import org.zionusa.event.domain.eventvolunteer.EventVolunteerManagementDao;
import org.zionusa.event.service.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class EventProposalsService extends BaseService<EventProposal, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventProposalsService.class);

    private final EventCategoryDao eventCategoryDao;
    private final EventProposalsDao eventProposalsDao;
    private final EventProposalVipDao eventProposalVipDao;
    private final EventProposalVipViewDao eventProposalVipViewDao;
    private final EventProposalTableViewDao eventProposalTableViewDao;
    private final EventStatusService eventStatusService;
    private final EventTeamsService eventTeamsService;
    private final EventVolunteerManagementDao eventVolunteerManagementDao;
    private final MessageSource messageSource;
    private final NotificationsService notificationsService;
    private final PermissionsAuthorizer permissionsAuthorizer;
    private final ResultsSurveysDao resultsSurveysDao;
    private final ResultsSurveysService resultsSurveysService;
    private final RestService restService;
    private final EventPresenterService eventPresenterService;
    private final EventPresenterDao eventPresenterDao;

    @Autowired
    public EventProposalsService(EventCategoryDao eventCategoryDao,
                                 EventProposalsDao eventProposalsDao,
                                 EventProposalVipDao eventProposalVipDao,
                                 EventProposalVipViewDao eventProposalVipViewDao,
                                 EventProposalTableViewDao eventProposalTableViewDao,
                                 EventStatusService eventStatusService,
                                 EventTeamsService eventTeamsService,
                                 EventVolunteerManagementDao eventVolunteerManagementDao,
                                 MessageSource messageSource,
                                 NotificationsService notificationsService,
                                 PermissionsAuthorizer permissionsAuthorizer,
                                 ResultsSurveysDao resultsSurveysDao,
                                 ResultsSurveysService resultsSurveysService,
                                 RestService restService, EventPresenterService eventPresenterService, EventPresenterDao eventPresenterDao) {
        super(eventProposalsDao, logger, EventProposal.class);
        this.eventCategoryDao = eventCategoryDao;
        this.eventStatusService = eventStatusService;
        this.eventProposalVipDao = eventProposalVipDao;
        this.eventProposalVipViewDao = eventProposalVipViewDao;
        this.eventProposalsDao = eventProposalsDao;
        this.eventProposalTableViewDao = eventProposalTableViewDao;
        this.eventTeamsService = eventTeamsService;
        this.eventVolunteerManagementDao = eventVolunteerManagementDao;
        this.messageSource = messageSource;
        this.notificationsService = notificationsService;
        this.permissionsAuthorizer = permissionsAuthorizer;
        this.resultsSurveysDao = resultsSurveysDao;
        this.resultsSurveysService = resultsSurveysService;
        this.restService = restService;
        this.eventPresenterService = eventPresenterService;
        this.eventPresenterDao = eventPresenterDao;
    }

    @PreAuthorize("hasAuthority('Admin')")
    public void createEventProposalCalendarInvite(HttpServletRequest request, Integer eventProposalId) throws NotFoundException, JsonProcessingException {
        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(eventProposalId);

        if (eventProposalOptional.isPresent()) {
            EventProposal eventProposal = eventProposalOptional.get();
            EventManagementBranch eventManagementBranch = eventTeamsService.getEventTeamByBranch(eventProposal.getBranchId());
            AtomicReference<EventManagementTeam> eventManagementTeam = new AtomicReference<>(new EventManagementTeam());

            eventManagementBranch.getTeams().forEach(eventTeam -> {
                if (eventTeam.getEventCategoryName().equals(eventProposal.getEventCategory().getTitle())) {
                    eventManagementTeam.set(eventTeam);
                }
            });

            restService.createCalendarInvite(request, eventManagementTeam.get(), eventProposal);
        }

        throw new NotFoundException("Event proposal id " + eventProposalId + " was not found");
    }

    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public void archive(Integer eventProposalId) throws NotFoundException {
        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(eventProposalId);
        if (eventProposalOptional.isPresent()) {
            EventProposal eventProposal = eventProposalOptional.get();
            eventProposal.setArchived(true);
            eventProposal.setPublished(false);
            save(eventProposal);
            logger.warn("Archiving event: {} with id {}", eventProposal.getTitle(), eventProposal.getId());
        } else {
            throw new NotFoundException("Event proposal id " + eventProposalId + " was not found");
        }

        ResultsSurvey resultsSurvey = resultsSurveysDao.getResultsSurveyByEventProposalId(eventProposalId);
        if (resultsSurvey != null) {
            resultsSurvey.setArchive(true);
            resultsSurveysService.save(resultsSurvey);
            logger.warn("Archiving result survey: {} with id: {}", resultsSurvey.getTitle(), resultsSurvey.getId());
        }
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<EventProposal> getAll(Boolean archived) {
        final List<EventProposal> eventProposals = super.getAll(archived);

        // Application role permissions filter
        return filterEventProposalsByUserRole(eventProposals);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')") // Restrict usage of this for now
    public Page<EventProposal> getAllByPage(Pageable pageable) {
        return super.getAllByPage(pageable);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        final AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();
        final List<EventProposal> eventProposals = permissionsAuthorizer.hasEventAdminReadAccess()
            // Admin views all results surveys
            ? eventProposalsDao.getAllByArchived(Boolean.TRUE.equals(archived))
            // Only view in your branch
            : eventProposalsDao.getAllByArchivedAndBranchIdOrWorkflowStatusContains(Boolean.TRUE.equals(archived), authenticatedUser.getChurchId(), "Approved");

        List<EventProposal> filteredEventProposals = filterEventProposalsByUserRole(eventProposals);

        // Application role permissions filter
        final List<Map<String, Object>> displayItems = new ArrayList<>();

        filteredEventProposals.forEach(item -> {
            Map<String, Object> displayContact = Util.getFieldsAndValues(columns, item);
            if (displayContact.size() > 0) displayItems.add(displayContact);
        });

        return displayItems;
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<EventProposalDisplay> getAllDisplayPreset() {
        final AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();
        final List<EventProposal> eventProposals = permissionsAuthorizer.hasEventAdminReadAccess()
            // Admin views all results surveys
            ? eventProposalsDao.getAllByArchived(false)
            // Only view in your branch
            : eventProposalsDao.getAllByArchivedAndBranchIdOrWorkflowStatusContains(false, authenticatedUser.getChurchId(), "Approved");

        // Application role permissions filter
        return filterEventProposalsByUserRole(eventProposals)
            .stream()
            .map(EventProposalDisplay::new)
            .collect(Collectors.toList());
    }

    // @PreAuthorize("Public")
    public List<EventProposalDisplay> getAllUpcoming() {
        String now = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
        return eventProposalsDao
            .getAllByIsPublishedAndIsPrivateAndProposedEndDateAfter(true, false, now)
            .stream()
            .map(EventProposalDisplay::new)
            .collect(Collectors.toList());
    }

    // @PreAuthorize("Public")
    public EventProposalDisplay getOneUpcoming(Integer eventProposalId) {
        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(eventProposalId);

        if (!eventProposalOptional.isPresent()) {
            throw new NotFoundException();
        }

        return new EventProposalDisplay(eventProposalOptional.get());
    }

    // @PreAuthorize("Public")
    public List<Map<String, Object>> getAllUpcoming(List<String> columns) {
        String now = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
        logger.warn("Retrieving all published, non private, upcoming display eventProposals ending after {}", now);

        List<EventProposal> eventProposals = eventProposalsDao.getAllByIsPublishedAndIsPrivateAndProposedEndDateAfter(true, false, now);

        return getAllDisplayFromList(eventProposals, columns);
    }

    @Override
    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public EventProposal save(EventProposal eventProposal) {
        logger.warn("Attempting to save event proposal: {} requested by: {}", eventProposal.getTitle(), eventProposal.getRequesterId());
        EventProposal returnedEventProposal = eventProposalsDao.save(eventProposal);

        // Create status workflow for this proposal if it does not already exist
        try {
            eventStatusService.getByEventProposalId(returnedEventProposal.getId());
        } catch (NotFoundException e) {
            // Does not exist so we are free to create it
            logger.warn("- Creating event status");
            eventStatusService.createEventStatus(returnedEventProposal);
        }

        // Create volunteer management list if it does not already exist
        List<EventVolunteerManagement> eventVolunteerManagements = eventVolunteerManagementDao.getEventVolunteerManagementsByEventProposalId(returnedEventProposal.getId());
        if (eventVolunteerManagements.isEmpty()) {
            List<EventVolunteerManagement> mainBranchEventVolunteerManagements = new ArrayList<>();
            mainBranchEventVolunteerManagements.add(new EventVolunteerManagement(
                returnedEventProposal,
                VolunteerType.CONTACT
            ));
            mainBranchEventVolunteerManagements.add(new EventVolunteerManagement(
                returnedEventProposal,
                VolunteerType.MEMBER
            ));
            mainBranchEventVolunteerManagements.add(new EventVolunteerManagement(
                returnedEventProposal,
                VolunteerType.VIP
            ));
            logger.warn("- Creating event volunteer management for the main branch");
            eventVolunteerManagementDao.saveAll(mainBranchEventVolunteerManagements);
        }

        return returnedEventProposal;
    }

    @Override
    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public List<EventProposal> saveMultiple(List<EventProposal> tList) {
        List<EventProposal> returnedEventProposals = new ArrayList<>();

        for (EventProposal eventProposal : tList) {
            EventProposal returnedEventProposal = save(eventProposal);
            returnedEventProposals.add(returnedEventProposal);
        }

        return returnedEventProposals;
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public EventProposal getById(Integer id) throws NotFoundException {
        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(id);

        if (eventProposalOptional.isPresent()) {
            EventProposal processedEventProposal = eventProposalOptional.get();

            final AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();
            // Get a map of the event categories the user can view
            final Map<Integer, Boolean> eventCategoryMap = getEventCategoriesUserCanAccessMap();

            if (EUserRole.CHURCH_LEADER.is(authenticatedUser.getRole()) || Boolean.TRUE.equals(eventCategoryMap.get(processedEventProposal.getEventCategoryId()))) {
                logger.warn("Retrieving Event Proposal with Id: {} and marketing materials: {}", processedEventProposal.getId(), processedEventProposal.getEventMarketing());
                return processedEventProposal;
            }
        }

        throw new NotFoundException("This event proposal does not exist.");
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public EventProposal patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public EventProposalDisplay getDisplayById(Integer eventProposalId) throws NotFoundException {
        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(eventProposalId);

        if (eventProposalOptional.isPresent()) {
            EventProposal eventProposal = eventProposalOptional.get();

            // Get a map of the event categories the user can view
            final Map<Integer, Boolean> eventCategoryMap = getEventCategoriesUserCanAccessMap();

            if (Boolean.TRUE.equals(eventCategoryMap.get(eventProposal.getEventCategoryId()))) {
                logger.warn("Retrieving Display Event Proposal with Id: {}", eventProposal.getId());
                return new EventProposalDisplay(eventProposalOptional.get());
            }
        }

        throw new NotFoundException("This event does not exist.");
    }

    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_EVENT_PROPOSAL + "')")
    public EventProposal finalizeById(Integer eventProposalId) {
        logger.warn("Attempting to mark existing eventProposal as final: {}", eventProposalId);
        try {
            EventProposal eventProposal = getById(eventProposalId);
            EventStatus eventStatus = eventProposal.getEventStatus();
            eventStatus.setFinalized(true);
            eventStatus.setFinalizedDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            eventStatusService.save(eventStatus);

            eventProposal.setWorkflowStatus(WorkflowStatus.SUBMITTED.status());
            return save(eventProposal);

        } catch (NotFoundException e) {
            logger.warn("A process flow was never created for this event proposal, cannot continue");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.DELETE_EVENT_PROPOSAL + "')")
    public void delete(Integer id) throws NotFoundException {
        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(id);

        if (!eventProposalOptional.isPresent())
            throw new NotFoundException("Cannot delete a eventProposal that does not exist");

        EventProposal eventProposal = eventProposalOptional.get();
        List<EventVolunteerManagement> eventVolunteerManagement = eventVolunteerManagementDao.getEventVolunteerManagementsByEventProposalId(eventProposal.getId());
        eventVolunteerManagement.forEach(eventVolunteerManagementDao::delete);

        logger.warn("Deleting eventProposal: {} with id: {}", eventProposal.getTitle(), eventProposal.getId());
        eventProposalsDao.delete(eventProposal);
    }

    @Override
    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.DELETE_EVENT_PROPOSAL + "')")
    public void deleteMultiple(String ids) throws NotFoundException {
        if (ids != null && !ids.isEmpty()) {
            String[] parsedIds = ids.split(",");

            for (String stringId : parsedIds) {
                Integer id = Integer.parseInt(stringId);
                delete(id);
            }
        }
    }

    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.PUBLISH_EVENT_PROPOSAL + "')")
    public void publishEventProposal(Integer id) throws ForbiddenException, NotFoundException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EventProposal eventProposal = super.getById(id);

        if (eventProposal == null) {
            throw new NotFoundException("Cannot publish an event proposal that does not exist");
        }

        // For security only certain roles can actually do publishing
        if (authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
            || (authenticatedUser.getUserApplicationRoles() != null && authenticatedUser.getUserApplicationRoles().contains("MY_ZIONUSA_EDITOR"))) {
            String publishedDate = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
            eventProposalsDao.publishEventProposal(id, publishedDate, authenticatedUser.getId(), authenticatedUser.getDisplayName());

            // If date is in the past do not send notification
            LocalDate parsedDate = LocalDate.parse(eventProposal.getProposedEndDate(), DateTimeFormatter.ISO_DATE);
            if (parsedDate.isAfter(LocalDate.now())) {
                // Send the notification to all  members
                String titleMessage = "notification.text.event.published.title";
                String contentMessage = "notification.text.event.published.message";

                Map<String, String> additionalData = new HashMap<>();
                additionalData.put("eventProposalId", eventProposal.getId().toString());
                additionalData.put("status", "Published");
                additionalData.put("type", "Event");

                // using the logged in users information send the request, we can get it from the token
                String[] contentParams = new String[]{
                    eventProposal.getTitle(),
                    eventProposal.getProposedDate(),
                    eventProposal.getProposedTime(),
                };

                try {
                    notificationsService.createAdminPushNotification(new ArrayList<>(), titleMessage, null, contentMessage, contentParams, additionalData);
                    recordNotification(eventProposal, Notification.EVENT_PROPOSAL, "published", titleMessage, contentMessage, contentParams, true);
                } catch (Exception e) {
                    recordNotification(eventProposal, Notification.EVENT_PROPOSAL, "published", titleMessage, contentMessage, contentParams, false);
                }
            }
        } else {
            throw new ForbiddenException();
        }
    }

    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.PUBLISH_EVENT_PROPOSAL + "')")
    public void unPublishEventProposal(Integer id) throws ForbiddenException, NotFoundException {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EventProposal eventProposal = super.getById(id);

        if (eventProposal == null)
            throw new NotFoundException("Cannot un-publish an event proposal that does not exist");

        // For security only certain roles can actually do publishing
        if (authenticatedUser.getAccess().equalsIgnoreCase("ADMIN")
            || (authenticatedUser.getUserApplicationRoles() != null && authenticatedUser.getUserApplicationRoles().contains("MY_ZIONUSA_EDITOR"))) {
            eventProposal.setEditorId(null);
            eventProposal.setEditorName(null);
            eventProposal.setPublished(false);
            eventProposal.setPublishedDate(null);
            super.save(eventProposal);
        } else {
            throw new ForbiddenException();
        }
    }

    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<EventProposalTableView> getEventProposalTableViewBy() {
        final AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();
        final boolean isAdmin = permissionsAuthorizer.hasEventAdminReadAccess();
        final boolean isManager = permissionsAuthorizer.hasEventManagerReadAccess();
        List<EventProposalTableView> eventProposalTableViews = (isAdmin || isManager)
            // Admin can view all event proposals.
            ? eventProposalTableViewDao.findAll()
            // All others only for their branch
            : eventProposalTableViewDao.getEventProposalTableViewByBranchId(authenticatedUser.getChurchId());
        // If Default, ASEZ or ASEZ Wao, the
        return filterEventProposalTableViewByUserRole(eventProposalTableViews);
    }

    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_UNPUBLISHED_EVENT_PROPOSAL + "')")
    public List<EventProposalTableView> getEventProposalTableViewByBranchId(Integer branchId) {
        List<EventProposalTableView> eventProposalTableViews = eventProposalTableViewDao.getEventProposalTableViewByBranchId(branchId);
        return filterEventProposalTableViewByUserRole(eventProposalTableViews);
    }

    private List<EventProposal> filterEventProposalsByUserRole(List<EventProposal> eventProposals) {
        final AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();
        // Get a map of the event categories the user can view
        final Map<Integer, Boolean> eventCategoryMap = getEventCategoriesUserCanAccessMap();
        // Filter event proposals by those categories
        return eventProposals
            .stream()
            .filter(eventProposal -> EUserRole.CHURCH_LEADER.is(authenticatedUser.getRole()) ||
                Boolean.TRUE.equals(eventCategoryMap.get(eventProposal.getEventCategoryId())))
            .collect(Collectors.toList());
    }

    private List<EventProposalTableView> filterEventProposalTableViewByUserRole(List<EventProposalTableView> eventProposalTableViews) {
        final AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();
        // Get a map of the event categories the user can view
        final Map<Integer, Boolean> eventCategoryMap = getEventCategoriesUserCanAccessMap();
        // Filter event proposals by those categories
        return eventProposalTableViews
            .stream()
            .filter(eventProposalTableView -> EUserRole.CHURCH_LEADER.is(authenticatedUser.getRole()) ||
                Boolean.TRUE.equals(eventCategoryMap.get(eventProposalTableView.getCategoryId())))
            .collect(Collectors.toList());
    }

    private Map<Integer, Boolean> getEventCategoriesUserCanAccessMap() {
        final AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();
        List<EventCategory> eventCategories = eventCategoryDao.findAllByActive(true)
            .stream()
            .filter(eventCategory -> {
                for (String applicationRole : authenticatedUser.getUserApplicationRoles()) {
                    if (eventCategory.getUserApplicationRoleBase() != null && (applicationRole.contains(eventCategory.getUserApplicationRoleBase()) || EApplicationRole.ADMIN_ACCESS.contains(applicationRole))) {
                        return true;
                    }
                }
                return false;
            })
            .collect(Collectors.toList());

        Map<Integer, Boolean> eventCategoryMap = new HashMap<>();
        eventCategories.forEach(eventCategory -> eventCategoryMap.putIfAbsent(eventCategory.getId(), true));
        return eventCategoryMap;
    }

    private void recordNotification(EventProposal eventProposal, String category, String subCategory, String titleMessage, String contentMessage, String[] contentParams, boolean processed) {
        Notification notification = new Notification();
        notification.setCategory(category);
        notification.setEventProposalId(eventProposal.getId());
        notification.setMessage(messageSource.getMessage(contentMessage, contentParams, Locale.ENGLISH));
        notification.setProcessed(processed);
        if (processed) {
            notification.setProcessTime(eventProposal.getPublishedDate());
        }
        notification.setRecipients("Members");
        notification.setSubCategory(subCategory);
        notification.setSubTitle("");
        notification.setTitle(messageSource.getMessage(titleMessage, contentParams, Locale.ENGLISH));
        notification.setType("text");

        notificationsService.save(notification);
    }

    public List<Map<String, Object>> getAllVipsByEventProposalId(@PathVariable Integer eventProposalId, List<String> columns) {
        List<EventProposalVipView> eventProposalVipViews = eventProposalVipViewDao.getAllByEventProposalId(eventProposalId);

        // Application role permissions filter
        final List<Map<String, Object>> displayItems = new ArrayList<>();

        eventProposalVipViews.forEach(item -> {
            Map<String, Object> displayContact = Util.getFieldsAndValues(columns, item);
            if (displayContact.size() > 0) displayItems.add(displayContact);
        });

        return displayItems;
    }

    public void deleteVipToEventProposal(Integer eventProposalId, Integer contactId) {
        EventProposalVip eventProposalVip = eventProposalVipDao.getAllByContactIdAndEventProposalId(contactId, eventProposalId);

        if (eventProposalVip != null) {
            eventProposalVipDao.delete(eventProposalVip);
        }
    }

    public void postVipToEventProposal(Integer eventProposalId, Integer contactId) {
        EventProposalVip previousEventProposalVip = eventProposalVipDao.getAllByContactIdAndEventProposalId(contactId, eventProposalId);

        if (previousEventProposalVip == null) {
            EventProposalVip eventProposalVip = new EventProposalVip();
            eventProposalVip.setContactId(contactId);
            eventProposalVip.setEventProposalId(eventProposalId);
            eventProposalVipDao.save(eventProposalVip);
        }
    }
}
