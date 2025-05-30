package org.zionusa.management.domain;

import lombok.Data;

import java.util.List;

@Data
public class CalendarEventCreateRequest {
    private Integer branchId;
    private String branchName;
    private String subject;
    private String bodyContent;
    private String bodyPreview;
    private String eventStartDateTime;
    private String eventEndDateTime;
    private String eventLink;
    private String eventTimeZone;
    private String locationAddress;
    private String locationCity;
    private String locationDisplayName;
    private String locationPostalCode;
    private String locationState;
    private String locationUri;
    private List<CalendarEventAttendee> attendeesList;
    private CalendarEventAttendee organizer;
}
