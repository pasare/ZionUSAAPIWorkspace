package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrackerUsageChurchReport {
    private String id;

    private Integer year;

    private Integer month;

    private Integer churchId;

    private String churchName;

    private Integer numOfActiveUsers;

    private Integer participantGoal;

    private double percentage;
}
