package org.zionusa.event.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventStatusNotes {
    private Integer eventStatusId;
    private String managerNotes;
    private String adminNotes;
    private String gaNotes;
}
