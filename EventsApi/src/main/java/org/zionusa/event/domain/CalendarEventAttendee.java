package org.zionusa.event.domain;

import lombok.Data;

@Data
public class CalendarEventAttendee {
    private String emailAddress;
    private String name;

    public CalendarEventAttendee(String name, String emailAddress) {
        this.name = name;
        this.emailAddress = emailAddress;
    }
}
