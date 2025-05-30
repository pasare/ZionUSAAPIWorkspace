package org.zionusa.event.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.util.constraints.DateFormatConstraint;
import org.zionusa.base.util.constraints.TimeFormatConstraint;
import org.zionusa.event.domain.eventProposal.EventProposal;
import org.zionusa.event.domain.eventStatus.EventStatus;
import org.zionusa.event.domain.eventType.EventType;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventProposalDisplay {

    @EqualsAndHashCode.Include
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    private Integer requesterId;

    private String requesterName;

    private String requesterEmail;

    private String title;


    private Integer branchId;

    private String branchName;

    private String description;

    private Integer eventCategoryId;

    private Integer eventTypeId;

    private String eventTypeName;

    @JsonIgnore
    private Integer eventStatusId;

    private Boolean informationalProposal;

    private Integer locationId;

    private String locationName;

    private boolean isInternal;

    private boolean isPrivate;

    private Integer vipsTotal;

    private Integer contactsTotal;

    private Integer membersTotal;

    private String logoUrl;

    private String backgroundUrl;

    @NotNull(message = "The proposed date is required")
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String proposedDate;

    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String proposedEndDate;

    @TimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private String proposedTime;

    @TimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private String proposedEndTime;

    private String workflowStatus;

    private EventStatus eventStatus;

    private String eventCategoryName;

    private String registrationRedirectUrl;

    public EventProposalDisplay(EventProposal eventProposal) {
        this.id = eventProposal.getId();
        this.requesterId = eventProposal.getRequesterId();
        this.title = eventProposal.getTitle();
        this.archived = eventProposal.getArchived();
        this.branchId = eventProposal.getBranchId();
        this.branchName = eventProposal.getBranchName();
        this.description = eventProposal.getDescription();
        this.eventCategoryId = eventProposal.getEventCategoryId();
        this.eventTypeId = eventProposal.getEventTypeId();
        this.eventCategoryName = "";
        this.eventTypeName = "";
        if (eventProposal.getEventCategory() != null) {
            this.eventCategoryName = eventProposal.getEventCategory().getTitle();
            Optional<EventType> optionalEventType = eventProposal.getEventCategory().getEventTypes().stream().findFirst();
            this.eventTypeName = optionalEventType.isPresent() ? optionalEventType.get().getTitle() : "";
        }
        this.eventStatusId = eventProposal.getEventStatusId();
        this.hidden = eventProposal.getHidden();
        this.informationalProposal = eventProposal.getInformationalProposal();
        this.isInternal = eventProposal.isInternal();
        this.isPrivate = eventProposal.isPrivate();
        this.locationId = eventProposal.getLocationId();
        this.locationName = eventProposal.getLocationName();
        this.vipsTotal = eventProposal.getVipsTotal();
        this.contactsTotal = eventProposal.getContactsTotal();
        this.membersTotal = eventProposal.getMembersTotal();
        this.logoUrl = eventProposal.getLogoUrl();
        this.backgroundUrl = eventProposal.getBackgroundUrl();
        this.proposedDate = eventProposal.getProposedDate();
        this.proposedEndTime = eventProposal.getProposedEndTime();
        this.proposedTime = eventProposal.getProposedTime();
        this.proposedEndTime = eventProposal.getProposedEndTime();
        this.workflowStatus = eventProposal.getWorkflowStatus();
        this.eventStatus = eventProposal.getEventStatus();
        this.registrationRedirectUrl = eventProposal.getRegistrationRedirectUrl();

    }
}
