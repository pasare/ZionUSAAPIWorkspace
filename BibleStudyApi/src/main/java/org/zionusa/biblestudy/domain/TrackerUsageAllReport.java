package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrackerUsageAllReport {
    private Integer year;

    private Integer month;

    private Integer numOfActiveUsers;

    private Integer participantGoal;
}
