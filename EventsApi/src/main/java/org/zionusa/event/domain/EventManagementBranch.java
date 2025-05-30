package org.zionusa.event.domain;

import lombok.Data;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventManagementBranch {
    @Id
    private Integer id;
    private List<EventManagementTeamView> leaders = new ArrayList<>();
    private String name;
    private List<EventManagementTeam> teams = new ArrayList<>();
}
