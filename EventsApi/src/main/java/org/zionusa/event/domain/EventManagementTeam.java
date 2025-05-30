package org.zionusa.event.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventManagementTeam {

    @EqualsAndHashCode.Include
    @JsonIgnore
    private Integer branchId;
    @JsonIgnore
    private String branchName;

    private List<EventManagementTeamView> eventAdmin = new ArrayList<>();
    private List<EventManagementTeamView> eventAssistant = new ArrayList<>();
    private List<EventManagementTeamView> eventAudioVisual = new ArrayList<>();
    private List<EventManagementTeamView> eventGraphicDesigner = new ArrayList<>();
    private List<EventManagementTeamView> eventManager = new ArrayList<>();
    private List<EventManagementTeamView> eventPhotographer = new ArrayList<>();
    private List<EventManagementTeamView> eventPublicRelations = new ArrayList<>();
    private List<EventManagementTeamView> eventRegistrar = new ArrayList<>();
    private List<EventManagementTeamView> eventRepresentative = new ArrayList<>();
    private List<EventManagementTeamView> eventSpokesperson = new ArrayList<>();
    private List<EventManagementTeamView> eventVideographer = new ArrayList<>();
    private List<EventManagementTeamView> eventGa = new ArrayList<>();

    private String eventCategoryName;
    private Integer eventCategoryId;
    private Map<String, String> rolesMap;

    EventManagementTeamView branchLeader;

    @Data
    public static class Editable {
        private String eventCategoryName;
        private Integer eventCategoryId;

        private List<String> eventAdmin = new ArrayList<>();
        private List<EventManagementTeamView> eventAssistant = new ArrayList<>();
        private List<EventManagementTeamView> eventAudioVisual = new ArrayList<>();
        private List<String> eventGa = new ArrayList<>();
        private List<EventManagementTeamView> eventGraphicDesigner = new ArrayList<>();
        private List<String> eventManager = new ArrayList<>();
        private List<EventManagementTeamView> eventPhotographer = new ArrayList<>();
        private List<EventManagementTeamView> eventPublicRelations = new ArrayList<>();
        private List<EventManagementTeamView> eventRegistrar = new ArrayList<>();
        private List<EventManagementTeamView> eventRepresentative = new ArrayList<>();
        private List<EventManagementTeamView> eventSpokesperson = new ArrayList<>();
        private List<EventManagementTeamView> eventVideographer = new ArrayList<>();
    }
}
