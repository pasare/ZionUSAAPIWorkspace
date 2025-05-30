package org.zionusa.event.domain.eventvolunteer;

import lombok.Data;

import java.util.List;

@Data
public class EventBranchVolunteers {
    private Integer branchId;
    private String branchName;
    private Boolean helpingBranch;
    private List<EventVolunteerManagement> members;
}
