package org.zionusa.management.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventManagementTeam {

    EventApprover churchLeader;
    private Integer requesterId;
    private String requesterEmail;
    private String requesterName;
    private Integer churchId;
    private String churchName;
    private List<EventApprover> eventRepresentatives;
    private List<EventApprover> managerApprovers;
    private List<EventApprover> adminApprovers;
    private List<EventApprover> gaApprovers;
    private List<EventApprover> graphicDesigners;
    private List<EventApprover> photographers;
    private List<EventApprover> publicRelationsRepresentatives;
    private List<EventApprover> spokespersons;
    private List<EventApprover> videographers;
    private List<EventApprover> audioVisualEngineers;

    public EventManagementTeam(String churchName, Integer churchId, List<User.ApplicationRole> eventRepresentatives, List<User.ApplicationRole> managerApprovers, List<User.ApplicationRole> adminApprover, List<User.ApplicationRole> gaApprover) {
        List<EventApprover> eventReps = new ArrayList<>();
        List<EventApprover> managers = new ArrayList<>();
        List<EventApprover> admins = new ArrayList<>();
        List<EventApprover> gas = new ArrayList<>();

        for (User.ApplicationRole representative : eventRepresentatives) {
            eventReps.add(new EventApprover(representative));
        }

        for (User.ApplicationRole manager : managerApprovers) {
            managers.add(new EventApprover(manager));
        }

        for (User.ApplicationRole admin : adminApprover) {
            admins.add(new EventApprover(admin));
        }

        for (User.ApplicationRole ga : gaApprover) {
            gas.add(new EventApprover(ga));
        }

        this.eventRepresentatives = eventReps;
        this.managerApprovers = managers;
        this.adminApprovers = admins;
        this.gaApprovers = gas;
        this.churchId = churchId;
        this.churchName = churchName;
    }

    public EventManagementTeam(String churchName,
                               Integer churchId,
                               List<User.ApplicationRole> eventRepresentatives,
                               List<User.ApplicationRole> managerApprovers,
                               List<User.ApplicationRole> adminApprover,
                               List<User.ApplicationRole> gaApprover,
                               List<User.ApplicationRole> graphicDesigners,
                               List<User.ApplicationRole> photographers,
                               List<User.ApplicationRole> publicRelationsRepresentatives,
                               List<User.ApplicationRole> videographers,
                               List<User.ApplicationRole> audioVisualEngineers
    ) {
        List<EventApprover> eventReps = new ArrayList<>();
        List<EventApprover> managers = new ArrayList<>();
        List<EventApprover> admins = new ArrayList<>();
        List<EventApprover> gas = new ArrayList<>();
        List<EventApprover> graphics = new ArrayList<>();
        List<EventApprover> photos = new ArrayList<>();
        List<EventApprover> prReps = new ArrayList<>();
        List<EventApprover> videos = new ArrayList<>();
        List<EventApprover> avs = new ArrayList<>();

        for (User.ApplicationRole representative : eventRepresentatives) {
            eventReps.add(new EventApprover(representative));
        }

        for (User.ApplicationRole manager : managerApprovers) {
            managers.add(new EventApprover(manager));
        }

        for (User.ApplicationRole admin : adminApprover) {
            admins.add(new EventApprover(admin));
        }

        for (User.ApplicationRole ga : gaApprover) {
            gas.add(new EventApprover(ga));
        }

        for (User.ApplicationRole graphic : graphicDesigners) {
            graphics.add(new EventApprover(graphic));
        }


        for (User.ApplicationRole photo : photographers) {
            photos.add(new EventApprover(photo));
        }

        for (User.ApplicationRole prRep : publicRelationsRepresentatives) {
            prReps.add(new EventApprover(prRep));
        }

        for (User.ApplicationRole video : videographers) {
            videos.add(new EventApprover(video));
        }

        for (User.ApplicationRole av : audioVisualEngineers) {
            avs.add(new EventApprover(av));
        }

        this.eventRepresentatives = eventReps;
        this.managerApprovers = managers;
        this.adminApprovers = admins;
        this.gaApprovers = gas;
        this.churchId = churchId;
        this.churchName = churchName;
        this.graphicDesigners = graphics;
        this.photographers = photos;
        this.publicRelationsRepresentatives = prReps;
        this.videographers = videos;
        this.audioVisualEngineers = avs;
    }
}
