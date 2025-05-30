package org.zionusa.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalendarEvent {
    private String name;
    private boolean isDone;
}
