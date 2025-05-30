package org.zionusa.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zionusa.base.enums.EDepartmentEmail;
import org.zionusa.event.domain.*;
import org.zionusa.event.domain.eventProposal.EventProposal;
import org.zionusa.event.domain.eventvolunteer.EventVolunteerManagement;
import org.zionusa.event.domain.eventvolunteer.EventVolunteerManagementDao;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static org.zionusa.base.util.auth.SecurityConstants.AUTHORIZATION;
import static org.zionusa.base.util.auth.SecurityConstants.X_APPLICATION_ID;

@Service
public class RestService {

    @Value("${zionusa.management.url}")
    private String managementUrl;
    @Value("${app.ui.url}")
    private String eventsUrl;
    @Value("${microsoft.o365.domain}")
    private String domain;
    @Value("${spring.mail.from}")
    private String eventEmail;
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

    @Value("${members.volunteers}")
    private String memberVolunteers;
    @Value("${social.media}")
    private String socialMedia;
    @Value("${social.media.lead}")
    private String socialMediaLead;

    private final EventVolunteerManagementDao eventVolunteerManagementDao;

    private static final Logger logger = LoggerFactory.getLogger(RestService.class);

    public RestService(EventVolunteerManagementDao eventVolunteerManagementDao) {
        this.eventVolunteerManagementDao = eventVolunteerManagementDao;
    }

    public static String convertToIsoTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("h:mm a")).format(DateTimeFormatter.ISO_TIME);
    }

    public CalendarEvent createCalendarInvite(HttpServletRequest request, EventManagementTeam eventManagementTeam, EventProposal eventProposal) throws JsonProcessingException {

        List<CalendarEventAttendee> calendarEventAttendeeList = new ArrayList<>();
        List<EventVolunteerManagement> volunteerManagementList = eventVolunteerManagementDao.getEventVolunteerManagementsByEventProposalId(eventProposal.getId());

        int totalContacts = 0;
        int totalMembers = 0;
        int totalVIPs = 0;

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

        String contentMessage = eventProposal.getBranchName() + "'s event " + eventProposal.getTitle() + " scheduled for "
                + LocalDate.parse(eventProposal.getProposedDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)) +
                ", at " + eventProposal.getProposedTime() + " has received approval from the Event Administrator. ";
        if (volunteerManagementList.isEmpty() || (totalContacts + totalMembers + totalVIPs) == 0) {
            logger.warn("No volunteers reported for this event.");
        } else {
            contentMessage += totalContacts + " guests, " + totalVIPs + " VIPs, and " + totalMembers + memberVolunteers + " are expected to attend.";
        }

        eventManagementTeam.getEventGa().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventManager().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventAdmin().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventAssistant().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventAudioVisual().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventGraphicDesigner().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventPhotographer().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventPublicRelations().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventRegistrar().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventRepresentative().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventSpokesperson().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        eventManagementTeam.getEventVideographer().forEach(attendee -> calendarEventAttendeeList.add(new CalendarEventAttendee(attendee.getDisplayName(), attendee.getUsername())));
        calendarEventAttendeeList.add(new CalendarEventAttendee("Graphic Team", graphicsEmail));
        calendarEventAttendeeList.add(new CalendarEventAttendee("Photo Team", photosEmail));
        calendarEventAttendeeList.add(new CalendarEventAttendee("Video Team", videoEmail));
        calendarEventAttendeeList.add(new CalendarEventAttendee("AV Team", avEmail));

        if (eventManagementTeam.getBranchLeader() != null) {
            calendarEventAttendeeList.add(new CalendarEventAttendee(eventManagementTeam.getBranchLeader().getDisplayName(), eventManagementTeam.getBranchLeader().getUsername()));
        }

        CalendarEventAttendee eventOrganizer = new CalendarEventAttendee(eventProposal.getRequesterEmail(), eventProposal.getRequesterName());

        CalendarEventCreateRequest calendarEventCreateRequest = new CalendarEventCreateRequest();
        calendarEventCreateRequest.setBranchId(eventProposal.getBranchId());
        calendarEventCreateRequest.setBranchName(eventProposal.getBranchName());
        calendarEventCreateRequest.setSubject(eventProposal.getTitle());
        calendarEventCreateRequest.setBodyContent(contentMessage);
        calendarEventCreateRequest.setBodyPreview("Save the date for " + eventProposal.getTitle() + " event");
        calendarEventCreateRequest.setEventStartDateTime(eventProposal.getProposedDate() + "T" + convertToIsoTime(eventProposal.getProposedTime()));
        calendarEventCreateRequest.setEventEndDateTime(eventProposal.getProposedEndDate() + "T" + convertToIsoTime(eventProposal.getProposedEndTime()));
        calendarEventCreateRequest.setEventLink(eventsUrl + "/admin/event-proposals/view/" + eventProposal.getId());
        // TODO: Later, base the timezone on the location -> calendarEventCreateRequest.setEventTimeZone("UTC");
        calendarEventCreateRequest.setLocationAddress(parseBoolean(eventProposal.getLocation().getAddress()) ? eventProposal.getLocation().getAddress() : eventProposal.getBranchName());
        calendarEventCreateRequest.setLocationCity(eventProposal.getLocation().getCity());
        calendarEventCreateRequest.setLocationDisplayName(eventProposal.getLocationName());
        calendarEventCreateRequest.setLocationPostalCode(eventProposal.getLocation().getZipPostalCode());
        calendarEventCreateRequest.setLocationState(eventProposal.getLocation().getStateProvince());
        calendarEventCreateRequest.setLocationUri(eventProposal.getLocation().getLinkedInUrl());
        calendarEventCreateRequest.setAttendeesList(calendarEventAttendeeList);
        calendarEventCreateRequest.setOrganizer(eventOrganizer);

        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.set(AUTHORIZATION, token);
            headers.set(X_APPLICATION_ID, request.getHeader(X_APPLICATION_ID));
            HttpEntity<CalendarEventCreateRequest> entity = new HttpEntity<>(calendarEventCreateRequest, headers);

            String url = managementUrl + "/calendar-event/microsoft";
            System.out.println("-------- management Url: " + url);
            CalendarEvent calendarEventHttpEntity = restTemplate.postForObject(url, entity, CalendarEvent.class);
            if (calendarEventHttpEntity != null) {
                return calendarEventHttpEntity;
            }
        }
        return null;
    }

}
