package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zionusa.base.enums.EApplicationRole;
import org.zionusa.base.enums.EDepartmentEmail;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.event.domain.*;
import org.zionusa.event.domain.eventCategory.EventCategory;
import org.zionusa.event.domain.eventCategory.EventCategoryDao;
import org.zionusa.event.domain.eventProposal.EventProposal;
import org.zionusa.event.domain.eventProposal.EventProposalsService;
import org.zionusa.event.domain.eventStatus.EventStatus;
import org.zionusa.event.domain.eventStatus.EventStatusService;
import org.zionusa.event.domain.eventTeam.EventTeamsService;
import org.zionusa.event.domain.eventvolunteer.EventVolunteerManagement;
import org.zionusa.event.domain.eventvolunteer.EventVolunteerManagementDao;
import org.zionusa.event.enums.ENotificationCategory;
import org.zionusa.event.enums.ENotificationMessageSourceType;
import org.zionusa.event.enums.ENotificationSubCategory;
import org.zionusa.event.enums.ENotificationType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowService.class);
    private final RestService restService;
    private final EventFundraisingService eventFundraisingService;
    private final EventStatusService eventStatusService;
    private final EventProposalsService eventProposalsService;
    private final NotificationsService notificationsService;
    private final MessageSource messageSource;
    private final EventTeamsService eventTeamsService;
    private final EventCategoryDao eventCategoryDao;
    private final EventVolunteerManagementDao eventVolunteerManagementDao;
    @Value("${app.ui.url}")
    private String eventsUrl;
    @Value("${microsoft.o365.domain}")
    private String domain;
    @Value("${spring.mail.from}")
    private String eventEmail;
    @Value("${spring.mail.from1}")
    private String eventCategory1Email;
    @Value("${spring.mail.from2}")
    private String eventCategory2Email;
    @Value("${greeting}")
    private String greeting;
    @Value("${head.office}")
    private String headOffice;
    @Value("${media.email}")
    private String mediaEmail;
    @Value("${photo.email}")
    private String photosEmail;
    @Value("${video.email}")
    private String videoEmail;
    @Value("${av.email}")
    private String avEmail;
    @Value("${editorial.email}")
    private String editorialEmail;
    @Value("${graphics.email}")
    private String graphicsEmail;
    @Value("${social.media}")
    private String socialMedia;
    @Value("${social.media.lead}")
    private String socialMediaLead;
    @Value("${closing.statement}")
    private String closingStatement;
    @Value("${event.category.default}")
    private String eventCategoryDefault;
    @Value("${event.category.title1}")
    private String eventCategoryTitle1;
    @Value("${event.category.title2}")
    private String eventCategoryTitle2;

    @Value("${members.volunteers}")
    private String memberVolunteers;
    @Autowired
    public WorkflowService(
        RestService restService,
        EventFundraisingService eventFundraisingService,
        EventStatusService eventStatusService,
        EventProposalsService eventProposalsService,
        NotificationsService notificationsService,
        MessageSource messageSource,
        EventTeamsService eventTeamsService,
        EventCategoryDao eventCategoryDao,
        EventVolunteerManagementDao eventVolunteerManagementDao) {
        this.restService = restService;
        this.eventFundraisingService = eventFundraisingService;
        this.eventStatusService = eventStatusService;
        this.eventProposalsService = eventProposalsService;
        this.notificationsService = notificationsService;
        this.messageSource = messageSource;
        this.eventTeamsService = eventTeamsService;
        this.eventCategoryDao = eventCategoryDao;
        this.eventVolunteerManagementDao = eventVolunteerManagementDao;
    }

    public EventProposal processApproval(HttpServletRequest request, EventProposal eventProposal, EventStatusNotes eventStatusNotes) {
        boolean finalizedProcessed = false;
        boolean managerApprovalProcessed = false;
        boolean adminApprovalProcessed = false;
        boolean gaApprovalProcessed = false;

        if (eventProposal != null) {
            logger.warn("==================================");
            logger.warn("===== EVENT PROPOSAL APPROVAL ====");
            logger.warn("==================================");
            logger.warn("The event proposal {} (id={}) is starting to process approval", eventProposal.getTitle(), eventProposal.getId());
            AtomicReference<EventManagementTeam> eventManagementTeam = new AtomicReference<>();
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            EventManagementBranch eventManagementBranch = eventTeamsService.getEventTeamByBranch(eventProposal.getBranchId());

            eventManagementBranch.getTeams().forEach(eventTeam -> {
                if (eventTeam.getEventCategoryId().equals(eventProposal.getEventCategoryId())) {
                    eventManagementTeam.set(eventTeam);
                }
            });
            if (eventManagementTeam.get() == null) {
                throw new NullPointerException("The event management team for requester: " + authenticatedUser.getDisplayName() + " could not be found, please ensure that the member is part of the correct event team");
            }
            // begin approval process, allow it to go to the highest level of approval possible
            if (eventProposal.getEventStatus() != null) {
                EventStatus eventStatus = eventProposal.getEventStatus();
                logger.warn("----------------------------------");
                logger.warn("Finalized:        {}", Boolean.TRUE.equals(eventStatus.getFinalized()) ? eventStatus.getFinalizedDate() : "No");
                logger.warn("Manager Approval: {}", Boolean.TRUE.equals(eventStatus.getManagerApproved()) ? eventStatus.getActualManagerDecisionDate() : "No");
                logger.warn("Admin Approval:   {}", Boolean.TRUE.equals(eventStatus.getAdminApproved()) ? eventStatus.getActualAdminDecisionDate() : "No");
                logger.warn("GA Submission:    {}", Boolean.TRUE.equals(eventStatus.getGaApproved()) ? eventStatus.getActualGaDecisionDate() : "No");
                logger.warn("----------------------------------");

                // event proposal has not been finalized yet. Cannot proceed
                if (Boolean.TRUE.equals(eventStatus.getFinalized()) && eventProposal.getEventStatus().getSubmitterName() == null) {
                    logger.warn("The event proposal {} is finalized", eventProposal.getTitle());
                    finalizedProcessed = processEventFinalized(request, authenticatedUser, eventManagementTeam.get(), eventProposal);
                } else if (Boolean.FALSE.equals(eventStatus.getFinalized())) {
                    logger.error("The event proposal is still pending finalization, the workflow cannot begin");
                } else {
                    logger.error("The event proposal is already submitted");
                }

                // The event proposal is pending manager approval
                if (eventStatus.getManagerApproved() == null || !eventStatus.getManagerApproved()) {
                    logger.warn("The event proposal {} is pending manager approval", eventProposal.getTitle());
                    managerApprovalProcessed = processEventManagerApproval(authenticatedUser, eventManagementTeam.get(), eventProposal, eventStatusNotes, false);
                } else {
                    logger.warn("The event proposal {} has already received manager approval, continuing...", eventProposal.getTitle());
                }

                // The event proposal is pending admin approval
                if (eventStatus.getAdminApproved() == null || !eventStatus.getAdminApproved()) {
                    logger.warn("The event proposal {} is pending admin approval", eventProposal.getTitle());
                    adminApprovalProcessed = processAdminApproval(request, authenticatedUser, eventManagementTeam.get(), eventProposal, eventStatusNotes);
                } else {
                    logger.warn("The event proposal {} has already received admin approval, continuing...", eventProposal.getTitle());
                }

                // The event proposal is pending GA decision, must first be submitted to GA
                if (eventStatus.getGaSubmitted() == null || !eventStatus.getGaSubmitted()) {
                    logger.warn("The event proposal {} is pending ga submission", eventProposal.getTitle());
                    gaApprovalProcessed = processGaApproval(authenticatedUser, eventManagementTeam.get(), eventProposal, eventStatusNotes);
                } else {
                    logger.warn("The event proposal {} has already received ga approval, nothing to do", eventProposal.getTitle());
                }

            } else {
                throw new NullPointerException("The event does not have an event status attached, cannot continue approval process");
            }
        }

        if (finalizedProcessed || managerApprovalProcessed || adminApprovalProcessed || gaApprovalProcessed) {
            logger.warn("The event {} received one or more of these approvals: Finalized = {}, Manager = {}, Admin = {}, Ga = {}",
                eventProposal.getTitle(), finalizedProcessed, managerApprovalProcessed, adminApprovalProcessed, gaApprovalProcessed);
            return eventProposalsService.getById(eventProposal.getId());
        }

        logger.warn("The approval process was not started, either the user does not have the correct role for the next step of approval, or there was an internal failure.");
        return null;
    }

    private boolean processEventFinalized(HttpServletRequest request,
                                          AuthenticatedUser authenticatedUser,
                                          EventManagementTeam eventManagementTeam,
                                          EventProposal eventProposal) {
        EventCategory eventCategory = new EventCategory();
        List<String> organizationEmail = new ArrayList<>();
        Optional<EventCategory> eventCategoryOptional = eventCategoryDao.findById(eventProposal.getEventCategoryId());
        if (eventCategoryOptional.isPresent()) {
            eventCategory = eventCategoryOptional.get();
            if (eventCategory.getEmail() != null) {
                organizationEmail.add(eventCategory.getEmail());
            }
            logger.warn("No organization email");
        }
        EventStatus eventStatus = eventProposal.getEventStatus();

        // Reject events with bad response to intro video and send Notification.

        if (eventProposal.getIntroVideoReaction().equals("Bad")) {
            logger.warn("The event proposal reaction was bad, so do not approve it");
            eventStatus.setManagerApproved(false);
            eventStatus.setActualManagerDecisionDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            if (eventStatus.getSubmitterId() == null && (eventStatus.getSubmitterName() == null || Objects.equals(eventStatus.getSubmitterName(), ""))) {
                eventStatus.setSubmitterId(authenticatedUser.getId());
                eventStatus.setSubmitterName(authenticatedUser.getDisplayName());
            }
            eventStatusService.save(eventStatus);
            eventProposal.setWorkflowStatus(WorkflowStatus.MANAGER_REJECTED.status());

            EventManagementTeamView eventRequester = new EventManagementTeamView();

            eventRequester.setUsername(authenticatedUser.getUsername());
            eventRequester.setUserId(authenticatedUser.getId());
            eventRequester.setDisplayName(authenticatedUser.getDisplayName());

            List<EventManagementTeamView> recipients = new ArrayList<>();
            recipients.add(eventRequester);
            Notification denialNotification = createDenialNotification(recipients, eventProposal);
            notificationsService.save(denialNotification);

        } else {
            eventStatus.setSubmitterId(authenticatedUser.getId());
            eventStatus.setSubmitterName(authenticatedUser.getDisplayName());
            eventStatusService.save(eventStatus);
            logger.warn("Requesting event manager for approval");
            if (eventManagementTeam.getEventManager() != null && !eventManagementTeam.getEventManager().isEmpty()) {
                logger.warn("Requesting event manager for approval");
                List<EventManagementTeamView> managerApprovers = new ArrayList<>(eventManagementTeam.getEventManager());
                Notification managerApprovalRequestedNotification = createApprovalRequestedNotification(managerApprovers, eventProposal, organizationEmail);
                if (managerApprovalRequestedNotification != null) {
                    notificationsService.save(managerApprovalRequestedNotification);
                }
            } else {
                logger.warn("Sending notification to Event Category email");
                List<EventManagementTeamView> approvers = new ArrayList<>();
                EventManagementTeamView organizationRecipient = new EventManagementTeamView();
                organizationRecipient.setUsername(eventCategory.getEmail());
                approvers.add(organizationRecipient);

                if (approvers.isEmpty()) {
                    logger.warn("No Organization or branch leader email available.");
                } else {
                    Notification branchLeaderNotification = branchLeaderNotification(approvers, eventProposal, organizationEmail);
                    notificationsService.save(branchLeaderNotification);
                    logger.warn("No event managers, so set this event as manager approved");
                    eventStatus.setManagerApproved(true);
                    eventStatus.setActualManagerDecisionDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
                    eventStatusService.save(eventStatus);
                    processEventManagerApproval(authenticatedUser, eventManagementTeam, eventProposal, new EventStatusNotes(), true);
                }
            }
        }
        return true;
    }


    public EventProposal processDenial(HttpServletRequest request, EventProposal eventProposal, EventStatusNotes eventStatusNotes) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String notes = "";

        if (validateEventManager(authenticatedUser)) {
            EventProposal modifiedEventProposal = setEventStatusDenied(eventProposal, eventStatusNotes);
            EventManagementTeamView eventRequester = new EventManagementTeamView();

            eventRequester.setUsername(authenticatedUser.getUsername());
            eventRequester.setUserId(authenticatedUser.getId());
            eventRequester.setDisplayName(authenticatedUser.getDisplayName());

            if ((eventStatusNotes.getManagerNotes() == null) || (eventStatusNotes.getManagerNotes().equals(""))) {
                logger.warn("No manager notes");
            } else if (eventStatusNotes.getManagerNotes() != null || !eventStatusNotes.getManagerNotes().equals("")) {
                notes = eventStatusNotes.getManagerNotes();
            }
            if ((eventStatusNotes.getAdminNotes() == null) || (eventStatusNotes.getAdminNotes().equals(""))) {
                logger.warn("No Admin notes");
            } else if (eventStatusNotes.getAdminNotes() != null || !eventStatusNotes.getAdminNotes().equals("")) {
                notes = eventStatusNotes.getAdminNotes();
            }
            if ((eventStatusNotes.getGaNotes() == null) || (eventStatusNotes.getGaNotes().equals(""))) {
                logger.warn("No GA notes");
            } else if (eventStatusNotes.getGaNotes() != null || !eventStatusNotes.getGaNotes().equals("")) {
                notes = eventStatusNotes.getGaNotes();
            } else {
                notes = " No notes. ";
            }


            List<EventManagementTeamView> recipients = new ArrayList<>();
            recipients.add(eventRequester);
            Notification denialNotification = createDenialNotification(recipients, eventProposal, notes);
            notificationsService.save(denialNotification);
            return eventProposalsService.save(modifiedEventProposal);
        }

        return null;
    }

    public EventFundraising processEventFundraisingApproval(HttpServletRequest request, EventFundraising eventFundraising) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (validateEventAdmin(authenticatedUser)) {
            logger.warn("Processing approval for event fundraising {} by userId {}", eventFundraising.getId(), authenticatedUser.getId());
            eventFundraising.setApproved(true);
            eventFundraising.setApprovedBy(authenticatedUser.getId());
            return eventFundraisingService.save(eventFundraising);
        }
        logger.warn("UserId {} is not allowed to approve eventFundraisingId {}", authenticatedUser.getId(), eventFundraising.getId());
        return null;
    }

    public EventFundraising processEventFundraisingDenial(HttpServletRequest request, EventFundraising eventFundraising) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (validateEventAdmin(authenticatedUser)) {
            logger.warn("Processing denial for eventFundraisingId {} by userId {}", eventFundraising.getId(), authenticatedUser.getId());
            eventFundraising.setApproved(false);
            eventFundraising.setApprovedBy(authenticatedUser.getId());
            return eventFundraisingService.save(eventFundraising);
        }
        logger.warn("UserId {} is not allowed to deny eventFundraisingId {}", authenticatedUser.getId(), eventFundraising.getId());
        return null;
    }

    private boolean processEventManagerApproval(AuthenticatedUser authenticatedUser,
                                                EventManagementTeam eventManagementTeam,
                                                EventProposal eventProposal,
                                                EventStatusNotes eventStatusNotes,
                                                boolean isAutoApproved
    ) {
        // ensure that the person approving is at least event manager
        if (validateEventManager(authenticatedUser) || isAutoApproved) {
            logger.warn("Event manager is doing the approval");
            try {
                EventProposal managerApprovedEventProposal = this.setEventStatusManagerApproved(eventProposal, eventStatusNotes);
                if (managerApprovedEventProposal != null) {
                    if (isAutoApproved) {
                        EventManagementTeamView eventManager = new EventManagementTeamView();

                        eventManager.setUsername("Auto Approver");
                        eventManager.setUserId(authenticatedUser.getId());
                        eventManager.setDisplayName("Auto Approver");

                        List<EventManagementTeamView> eventManagers = new ArrayList<>();
                        eventManagers.add(eventManager);
                        eventManagementTeam.setEventManager(eventManagers);
                        managerApprovedEventProposal.getEventStatus().setManagerApproverId(null);
                        managerApprovedEventProposal.getEventStatus().setManagerApproverName("Auto Approved");

                    } else {
                        managerApprovedEventProposal.getEventStatus().setManagerApproverId(authenticatedUser.getId());
                        managerApprovedEventProposal.getEventStatus().setManagerApproverName(authenticatedUser.getDisplayName());
                    }
                    eventStatusService.save(managerApprovedEventProposal.getEventStatus());
                    eventProposalsService.save(managerApprovedEventProposal);

                    Notification notification = createApprovalGrantedNotification(ApprovalType.MANAGER.getValue(), eventProposal.getRequesterEmail(), eventProposal, eventManagementTeam);
                    if (notification != null) {
                        logger.warn("Event requester email notification: {}", eventProposal.getRequesterEmail());
                        notificationsService.save(notification);
                    }

                    Notification adminApprovalRequestedNotification = createApprovalRequestedNotification(eventManagementTeam.getEventAdmin(), eventProposal);
                    if (adminApprovalRequestedNotification != null) {
                        logger.warn("Event admin email {} :", eventManagementTeam.getEventAdmin());
                        notificationsService.save(adminApprovalRequestedNotification);
                    }

                    return true;
                } else {
                    logger.error("There was a error processing this event proposal");
                    return false;
                }
            } catch (Exception e) {
                logger.error("There was a error processing this event proposal", e);
                return false;
            }
        }
        return false;
    }

    private boolean processAdminApproval(HttpServletRequest request,
                                         AuthenticatedUser authenticatedUser,
                                         EventManagementTeam eventManagementTeam,
                                         EventProposal eventProposal,
                                         EventStatusNotes eventStatusNotes
    ) {
        if (validateEventAdmin(authenticatedUser)) {
            logger.warn("Admin is doing the approval");
            try {
                EventProposal adminApprovedEventProposal = this.setEventStatusAdminApproved(eventProposal, eventStatusNotes);

                if (adminApprovedEventProposal != null) {
                    List<String> organizationEmail = new ArrayList<>();
                    organizationEmail.add(graphicsEmail);
                    organizationEmail.add(photosEmail);
                    organizationEmail.add(videoEmail);
                    organizationEmail.add(avEmail);

                    adminApprovedEventProposal.getEventStatus().setAdminApproverId(authenticatedUser.getId());
                    adminApprovedEventProposal.getEventStatus().setAdminApproverName(authenticatedUser.getDisplayName());
                    eventStatusService.save(adminApprovedEventProposal.getEventStatus());
                    eventProposalsService.save(adminApprovedEventProposal);

                    Notification notification = createApprovalGrantedNotification(ApprovalType.ADMIN.getValue(), eventProposal.getRequesterEmail(), eventProposal, eventManagementTeam);
                    if (notification != null) {
                        logger.warn("Event requester email notification: {}", eventProposal.getRequesterEmail());
                        notificationsService.save(notification);
                    }

                    Notification multimediaNotification = createMultiMediaNotification(eventManagementTeam.getEventAdmin(), eventProposal, organizationEmail, eventManagementTeam);
                    if (multimediaNotification != null) {
                        logger.warn("Multimedia department email notification: {}", eventProposal.getRequesterEmail());
                        notificationsService.save(multimediaNotification);
                    }

                    Notification gaApprovalRequestedNotification = createApprovalRequestedNotification(eventManagementTeam.getEventGa(), eventProposal);
                    if (gaApprovalRequestedNotification != null) {
                        logger.warn("Event GA approver email {} :", eventManagementTeam.getEventGa());
                        notificationsService.save(gaApprovalRequestedNotification);
                    }

                    restService.createCalendarInvite(request, eventManagementTeam, eventProposal);

                    return true;
                } else {
                    logger.error("There was a error processing this event proposal");
                    return false;
                }
            } catch (Exception e) {
                logger.error("There was a error processing this event proposal", e);
                return false;
            }
        }
        return false;
    }

    public boolean processGaApproval(AuthenticatedUser authenticatedUser,
                                     EventManagementTeam eventManagementTeam,
                                     EventProposal eventProposal,
                                     EventStatusNotes eventStatusNotes) {

        if (validateEventGA(authenticatedUser)) {
            try {
                EventProposal gaApprovedEventProposal = this.setEventStatusGaApproved(eventProposal, eventStatusNotes);

                if (gaApprovedEventProposal != null) {
                    gaApprovedEventProposal.getEventStatus().setGaApproverId(authenticatedUser.getId());
                    gaApprovedEventProposal.getEventStatus().setGaApproverName(authenticatedUser.getDisplayName());
                    eventStatusService.save(gaApprovedEventProposal.getEventStatus());
                    eventProposalsService.save(gaApprovedEventProposal);

                    // Notify create that their request has been approved
                    Notification notification = createApprovalGrantedNotification(ApprovalType.GA.getValue(), eventProposal.getRequesterEmail(), eventProposal, eventManagementTeam);
                    if (notification != null) {
                        notificationsService.save(notification);
                    }

                    return true;
                } else {
                    logger.error("The event proposal could not be found, cannot continue");
                    return false;
                }
            } catch (Exception e) {
                logger.error("There was a error processing this event proposal", e);
                return false;
            }
        }
        return false;
    }

    private boolean validateEventManager(AuthenticatedUser authenticatedUser) {
        for (String applicationRole : authenticatedUser.getUserApplicationRoles()) {
            if (EApplicationRole.EVENT_DEFAULT_MANAGER.is(applicationRole) || EApplicationRole.EVENT_ASEZ_IUBA_MANAGER.is(applicationRole) || EApplicationRole.EVENT_ASEZ_WAO_IWBA_MANAGER.is(applicationRole)) {
                return true;
            }
        }
        if (validateEventAdmin(authenticatedUser)) {
            return true;
        }
        logger.warn("The user {} does not have the role of MANAGER or ADMIN", authenticatedUser.getDisplayName());
        return false;
    }

    public boolean validateEventAdmin(AuthenticatedUser authenticatedUser) {
        for (String applicationRole : authenticatedUser.getUserApplicationRoles()) {
            if (EApplicationRole.ADMIN_ACCESS.is(applicationRole) || EApplicationRole.EVENT_DEFAULT_ADMIN.is(applicationRole) || EApplicationRole.EVENT_ASEZ_IUBA_ADMIN.is(applicationRole) || EApplicationRole.EVENT_ASEZ_WAO_IWBA_ADMIN.is(applicationRole))
                return true;
        }
        logger.warn("The user {} does not have the role of ADMIN", authenticatedUser.getDisplayName());
        return false;
    }

    public boolean validateEventGA(AuthenticatedUser authenticatedUser) {
        for (String applicationRole : authenticatedUser.getUserApplicationRoles()) {
            if (EApplicationRole.EVENT_DEFAULT_GA.is(applicationRole) || EApplicationRole.EVENT_ASEZ_IUBA_GA.is(applicationRole) || EApplicationRole.EVENT_ASEZ_WAO_IWBA_GA.is(applicationRole))
                return true;
        }
        logger.warn("The user {} does not have the role of GA", authenticatedUser.getDisplayName());
        return false;
    }


    public EventProposal setEventStatusFinalized(EventProposal eventProposal) {
        if (eventProposal.getEventStatus().getStarted() == null || !eventProposal.getEventStatus().getStarted()) {
            eventProposal.getEventStatus().setStarted(true);
            eventProposal.getEventStatus().setStartedDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            eventProposal.setWorkflowStatus(WorkflowStatus.PENDING.status());
        }

        if (eventProposal.getEventStatus().getFinalized() == null || !eventProposal.getEventStatus().getFinalized()) {
            eventProposal.getEventStatus().setFinalized(true);
            eventProposal.getEventStatus().setFinalizedDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            eventProposal.setWorkflowStatus(WorkflowStatus.SUBMITTED.status());

        }

        return eventProposal;
    }

    public EventProposal setEventStatusManagerApproved(EventProposal eventProposal, EventStatusNotes eventStatusNotes) {
        // do the previous step
        EventProposal finalizedEventProposal = this.setEventStatusFinalized(eventProposal);

        if (eventProposal.getEventStatus().getManagerApproved() == null || !finalizedEventProposal.getEventStatus().getManagerApproved()) {
            finalizedEventProposal.getEventStatus().setManagerApproved(true);
            finalizedEventProposal.getEventStatus().setActualManagerDecisionDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            if (eventStatusNotes != null)
                finalizedEventProposal.getEventStatus().setManagerNotes(eventStatusNotes.getManagerNotes());
            finalizedEventProposal.setWorkflowStatus(WorkflowStatus.MANAGER_APPROVED.status());
        }

        return finalizedEventProposal;
    }

    public EventProposal setEventStatusDenied(EventProposal eventProposal, EventStatusNotes eventStatusNotes) {
        if (eventProposal.getEventStatus() != null) {
            EventStatus eventStatus = eventProposal.getEventStatus();
            WorkflowStatus workflowStatus = WorkflowStatus.getByName(eventProposal.getWorkflowStatus());
            if (workflowStatus != null) {

                switch (workflowStatus) {
                    case ADMIN_APPROVED:
                        // the event was approved by admin, but rejected by the ga approver
                        eventStatus.setGaSubmitted(null);
                        eventStatus.setGaApproved(false);
                        eventStatus.setGaNotes(eventStatusNotes.getGaNotes());
                        eventProposal.setWorkflowStatus(WorkflowStatus.GA_REJECTED.status());
                        break;
                    case MANAGER_APPROVED:
                        //the event was approved by manager but rejected by admin
                        eventStatus.setAdminApproved(false);
                        eventStatus.setAdminNotes(eventStatusNotes.getAdminNotes());
                        eventProposal.setWorkflowStatus(WorkflowStatus.ADMIN_REJECTED.status());
                        break;
                    case SUBMITTED:
                        //the event was submitted but manager rejected
                        eventStatus.setManagerApproved(false);
                        eventStatus.setManagerNotes(eventStatusNotes.getManagerNotes());
                        eventProposal.setWorkflowStatus(WorkflowStatus.MANAGER_REJECTED.status());
                }
                eventStatus.setAdminApproved(false);
                eventStatus.setAdminNotes(eventStatusNotes.getAdminNotes());
                eventProposal.setWorkflowStatus(WorkflowStatus.ADMIN_REJECTED.status());
            }

            eventStatusService.save(eventStatus);
        }

        return eventProposal;
    }

    public EventProposal setEventStatusAdminApproved(EventProposal eventProposal, EventStatusNotes eventStatusNotes) {
        // do the previous step
        EventProposal managerApprovedProposal = this.setEventStatusManagerApproved(eventProposal, eventStatusNotes);

        if (eventProposal.getEventStatus().getAdminApproved() == null || !managerApprovedProposal.getEventStatus().getAdminApproved()) {
            managerApprovedProposal.getEventStatus().setAdminApproved(true);
            managerApprovedProposal.getEventStatus().setActualAdminDecisionDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            if (eventStatusNotes != null)
                managerApprovedProposal.getEventStatus().setAdminNotes(eventStatusNotes.getAdminNotes());
            managerApprovedProposal.setWorkflowStatus(WorkflowStatus.ADMIN_APPROVED.status());
        }

        return managerApprovedProposal;
    }

    public EventProposal setEventStatusGaApproved(EventProposal eventProposal, EventStatusNotes eventStatusNotes) {
        //do the previous step
        EventProposal adminApprovedProposal = this.setEventStatusAdminApproved(eventProposal, eventStatusNotes);

        if (adminApprovedProposal.getEventStatus().getGaApproved() == null || !adminApprovedProposal.getEventStatus().getGaApproved()) {
            adminApprovedProposal.getEventStatus().setGaApproved(true);
            adminApprovedProposal.getEventStatus().setActualGaDecisionDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            if (eventStatusNotes != null)
                adminApprovedProposal.getEventStatus().setGaNotes(eventStatusNotes.getGaNotes());
            adminApprovedProposal.setWorkflowStatus(WorkflowStatus.GA_APPROVED.status());
        }
        return adminApprovedProposal;
    }

    public Notification createApprovalGrantedNotification(String approvalType, String requester, EventProposal eventProposal, EventManagementTeam eventManagementTeam) {
        String[] messageParams;
        EventManagementTeamView eventManager = null;
        String eventCategoryEmail = "";
        try {
            if (!eventManagementTeam.getEventManager().isEmpty()) {
                eventManager = eventManagementTeam.getEventManager().get(0);
            }
            logger.warn("No event manager was found for this branch");
            eventManager = eventManagementTeam.getEventAdmin().get(0);
        } catch (NullPointerException e) {
            logger.warn("No event manager was found for this branch, {}", e.getMessage());
        }

        if(eventProposal.getEventCategory().getTitle().equalsIgnoreCase(eventCategoryDefault)) {
            eventCategoryEmail = eventEmail;
        } else if (eventProposal.getEventCategory().getTitle().equalsIgnoreCase(eventCategoryTitle1)) {
            eventCategoryEmail = eventCategory1Email;
        } else if (eventProposal.getEventCategory().getTitle().equalsIgnoreCase(eventCategoryTitle2)) {
            eventCategoryEmail = eventCategory2Email;
        }

        LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate());

        String[] titleParams = new String[]{
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventProposal.getProposedTime()
        };

        if (approvalType.equals(ApprovalType.ADMIN.getValue())) {
            messageParams = new String[]{
                greeting,
                eventProposal.getTitle(),
                DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
                eventProposal.getProposedTime(),
                eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId(),
                eventCategoryEmail,
                eventManager.getDisplayName(),
                headOffice,
                mediaEmail,
                photosEmail,
                videoEmail,
                editorialEmail,
                socialMedia,
                socialMediaLead,
                eventManager.getUsername(),
                eventProposal.getBranchName(),
                eventProposal.getRequesterName(),
                closingStatement,

            };
        } else {
            messageParams = new String[]{
                eventProposal.getTitle(),
                DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
                eventProposal.getProposedTime(),
                eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId(),
                eventManagementTeam.getEventManager().get(0).getDisplayName(),
                eventManagementTeam.getEventManager().get(0).getUsername(),
                eventProposal.getBranchName(),
                eventProposal.getRequesterName(),
            };
        };
        String title = messageSource.getMessage(ENotificationMessageSourceType.APPROVAL_TITLE.getValue() + approvalType, titleParams, Locale.ENGLISH);
        String message = messageSource.getMessage(ENotificationMessageSourceType.APPROVAL_MESSAGE.getValue() + approvalType, messageParams, Locale.ENGLISH);

        EventManagementTeamView eventRequester = new EventManagementTeamView();
        eventRequester.setUsername(requester);

        return createNotification(Collections.singletonList(eventRequester), ENotificationCategory.EVENT_NOTIFICATION.getValue(), ENotificationSubCategory.APPROVAL_GRANTED.getValue(), title, message, Notification.TYPE_EMAIL);
    }

    public Notification createApprovalRequestedNotification(List<EventManagementTeamView> approvers, EventProposal eventProposal) {
        LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate());

        String[] titleParams = new String[]{
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventProposal.getProposedTime()
        };

        String[] messageParams = new String[]{
            eventProposal.getTitle(),
            eventProposal.getRequesterName(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId(),
            eventProposal.getProposedTime(),

        };
        String title = messageSource.getMessage(ENotificationMessageSourceType.REQUEST_TITLE.getValue(), titleParams, Locale.ENGLISH);
        String message = messageSource.getMessage(ENotificationMessageSourceType.REQUEST_MESSAGE.getValue(), messageParams, Locale.ENGLISH);

        return createNotification(approvers, ENotificationCategory.EVENT_NOTIFICATION.getValue(), ENotificationSubCategory.APPROVAL_REQUESTED.getValue(), title, message, Notification.TYPE_EMAIL);
    }

    public Notification createApprovalRequestedNotification(List<EventManagementTeamView> approvers, EventProposal eventProposal, List<String> organizationEmail) {

        LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate());
        String[] titleParams = new String[]{
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventProposal.getProposedTime()
        };

        String[] messageParams = new String[]{
            eventProposal.getTitle(),
            eventProposal.getRequesterName(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId()
        };
        String title = messageSource.getMessage(ENotificationMessageSourceType.REQUEST_TITLE.getValue(), titleParams, Locale.ENGLISH);
        String message = messageSource.getMessage(ENotificationMessageSourceType.REQUEST_MESSAGE.getValue(), messageParams, Locale.ENGLISH);

        return createNotification(approvers, ENotificationCategory.EVENT_NOTIFICATION.getValue(), ENotificationSubCategory.APPROVAL_REQUESTED.getValue(), title, message, Notification.TYPE_EMAIL, organizationEmail);
    }

    public Notification branchLeaderNotification(List<EventManagementTeamView> approvers, EventProposal eventProposal, List<String> organizationEmail) {
        LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate());
        String[] titleParams = new String[]{
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventProposal.getProposedTime()
        };

        String[] messageParams = new String[]{
            eventProposal.getTitle(),
            eventProposal.getRequesterName(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId(),
            eventProposal.getBranchName(),

        };
        String title = messageSource.getMessage(ENotificationMessageSourceType.REQUEST_TITLE.getValue(), titleParams, Locale.ENGLISH);
        String message = messageSource.getMessage(ENotificationMessageSourceType.BRANCH_MESSAGE.getValue(), messageParams, Locale.ENGLISH);

        return createNotification(approvers, ENotificationCategory.EVENT_NOTIFICATION.getValue(), ENotificationSubCategory.APPROVAL_REQUESTED.getValue(), title, message, Notification.TYPE_EMAIL, organizationEmail);
    }

    public Notification createMultiMediaNotification(List<EventManagementTeamView> approvers, EventProposal eventProposal, List<String> organizationEmail, EventManagementTeam eventManagementTeam) {
        LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate());
        List<EventVolunteerManagement> volunteerManagementList = eventVolunteerManagementDao.getEventVolunteerManagementsByEventProposalId(eventProposal.getId());

        int totalContacts = 0;
        int totalMembers = 0;
        int totalVIPs = 0;
        String participantCount = "";

        for (EventVolunteerManagement volunteers : volunteerManagementList) {
            if (volunteers.getType().equals(VolunteerType.CONTACT)) {
                totalContacts = volunteers.getFemaleAdults() + volunteers.getMaleAdults() +
                    volunteers.getFemaleCollegeStudents() + volunteers.getMaleCollegeStudents() +
                    volunteers.getFemaleYoungAdults() + volunteers.getMaleYoungAdults() +
                    volunteers.getFemaleTeenagers() + volunteers.getMaleTeenagers();
            }
            if (volunteers.getType().equals(VolunteerType.MEMBER)) {
                totalMembers = volunteers.getFemaleAdults() + volunteers.getMaleAdults() +
                    volunteers.getFemaleCollegeStudents() + volunteers.getMaleCollegeStudents() +
                    volunteers.getFemaleYoungAdults() + volunteers.getMaleYoungAdults() +
                    volunteers.getFemaleTeenagers() + volunteers.getMaleTeenagers();
            }
            if (volunteers.getType().equals(VolunteerType.VIP)) {
                totalVIPs = volunteers.getFemaleAdults() + volunteers.getMaleAdults() +
                    volunteers.getFemaleCollegeStudents() + volunteers.getMaleCollegeStudents() +
                    volunteers.getFemaleYoungAdults() + volunteers.getMaleYoungAdults() +
                    volunteers.getFemaleTeenagers() + volunteers.getMaleTeenagers();
            }
        }

        if (volunteerManagementList.isEmpty() || (totalContacts + totalMembers + totalVIPs) == 0) {
            logger.warn("No volunteers reported for this event.");
        } else {
            participantCount = totalContacts + " guests, " + totalVIPs + " VIPs, and " + totalMembers + memberVolunteers + " are expected to attend.";
        }

        String branchLeader = getBranchLeader(eventProposal);
        String location = getLocation(eventProposal);
        String[] titleParams = new String[]{
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventProposal.getProposedTime()
        };

        String[] messageParams = new String[]{
            eventProposal.getBranchName(),
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId(),
            eventProposal.getRequesterName(),
            location,
            eventProposal.getIndoorOutdoor(),
            eventProposal.getEventCategory().getTitle(),
            participantCount,
            branchLeader,
        };
        String title = messageSource.getMessage(ENotificationMessageSourceType.MULTIMEDIA_APPROVAL_TITLE.getValue(), titleParams, Locale.ENGLISH);
        String message = messageSource.getMessage(ENotificationMessageSourceType.MULTIMEDIA_APPROVAL_MESSAGE.getValue(), messageParams, Locale.ENGLISH);

        return createNotification(approvers, ENotificationCategory.EVENT_NOTIFICATION.getValue(), ENotificationSubCategory.APPROVAL_GRANTED.getValue(), title, message, Notification.TYPE_EMAIL, organizationEmail);
    }

    public Notification createDenialNotification(List<EventManagementTeamView> approvers, EventProposal eventProposal) {
        LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate());
        String[] titleParams = new String[]{
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventProposal.getProposedTime()
        };
        String[] messageParams = new String[]{
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventProposal.getProposedTime(),
            eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId()

        };
        String title = messageSource.getMessage(ENotificationMessageSourceType.DENIED_TITLE.getValue(), titleParams, Locale.ENGLISH);
        String message = messageSource.getMessage(ENotificationMessageSourceType.DENIED_MESSAGE.getValue(), messageParams, Locale.ENGLISH);
        return createNotification(approvers, ENotificationCategory.EVENT_NOTIFICATION.getValue(), ENotificationSubCategory.APPROVAL_DENIED.getValue(), title, message, Notification.TYPE_EMAIL);
    }

    public Notification createDenialNotification(List<EventManagementTeamView> approvers, EventProposal eventProposal, String eventStatusNotes) {
        LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate());
        String[] titleParams = new String[]{
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventProposal.getProposedTime()
        };
        String[] messageParams = new String[]{
            eventProposal.getTitle(),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate),
            eventProposal.getProposedTime(),
            eventStatusNotes,
            eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId()

        };
        String title = messageSource.getMessage(ENotificationMessageSourceType.DENIED_TITLE.getValue(), titleParams, Locale.ENGLISH);
        String message = messageSource.getMessage(ENotificationMessageSourceType.DENIED_MESSAGE.getValue(), messageParams, Locale.ENGLISH);
        return createNotification(approvers, ENotificationCategory.EVENT_NOTIFICATION.getValue(), ENotificationSubCategory.APPROVAL_DENIED.getValue(), title, message, ENotificationType.EMAIL.getValue());
    }

    public Notification createNotification(List<EventManagementTeamView> approvers, String category, String subCategory,
                                           String title, String message, String type, List<String> organizationEmail) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setCategory(category);
        notification.setSubCategory(subCategory);
        notification.setMessage(message);
        notification.setType(type);
        notification.setProcessed(false);

        StringBuilder approversString = new StringBuilder();
        StringBuilder bccRecipientString = new StringBuilder();
        for (EventManagementTeamView approver : approvers) {
            if (approver != null && approver.getUsername() != null) {
                approversString.append(approver.getUsername()).append(", ");
            }
        }
        for (String orgEmail : organizationEmail) {
            bccRecipientString.append(orgEmail).append(", ");
        }
        logger.warn("Event Approvers OrgEmails {} :", approversString);

        if (approversString.length() > 0) {
            notification.setRecipients(approversString.substring(0, approversString.length() - 2));
        } else {
            logger.warn("A notification cannot be created without a recipient list");
        }

        if (bccRecipientString.length() > 0)
            notification.setBccRecipients(bccRecipientString.substring(0, bccRecipientString.length() - 2));

        return notification;
    }

    public Notification createNotification(List<EventManagementTeamView> approvers, String category, String subCategory,
                                           String title, String message, String type) {

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setCategory(category);
        notification.setSubCategory(subCategory);
        notification.setMessage(message);
        notification.setType(type);
        notification.setProcessed(false);

        StringBuilder approversString = new StringBuilder();
        for (EventManagementTeamView approver : approvers) {
            logger.warn("Event Approvers {} :", approvers);
            if (approver != null && approver.getUsername() != null) {
                approversString.append(approver.getUsername()).append(", ");
            }
        }
        if (approversString.length() > 0) {
            notification.setRecipients(approversString.substring(0, approversString.length() - 2));
        } else {
            throw new IllegalStateException("A notification cannot be created without a recipient list");
        }
        return notification;
    }

    private String getBranchLeader(EventProposal eventProposal) {
        List<EventManagementTeamView> leaders;
        String leaderList = null;
        if ((eventTeamsService.getEventTeamByBranch(eventProposal.getBranchId())).getLeaders() != null) {
            leaders = eventTeamsService.getEventTeamByBranch(eventProposal.getBranchId()).getLeaders();
            for (EventManagementTeamView leader : leaders) {
                if (leader.getDisplayName() != null)
                    leaderList += leader.getDisplayName() + ",";
            }

            return leaderList;
        } else return "No leader registered for this branch";
    }

    private String getLocation(EventProposal eventProposal) {
        if (eventProposal.getLocation() != null && eventProposal.getLocation().getLocationName() != null)
            return (eventProposal.getLocation().getLocationName());
        else
            return "Location is most likely " + eventProposal.getBranchName() + "branch location. Confirm location with event representative if needed.";
    }

}
