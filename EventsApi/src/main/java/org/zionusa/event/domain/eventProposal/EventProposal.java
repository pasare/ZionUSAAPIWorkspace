package org.zionusa.event.domain.eventProposal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;
import org.zionusa.base.util.constraints.TimeFormatConstraint;
import org.zionusa.event.domain.*;
import org.zionusa.event.domain.eventCampaign.EventCampaign;
import org.zionusa.event.domain.eventPresenter.EventPresenter;
import org.zionusa.event.domain.eventCategory.EventCategory;
import org.zionusa.event.domain.eventStatus.EventStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@AllArgsConstructor
@Table(name = "event_proposals")
public class EventProposal implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    @NotNull(message = "The id of the requester is required")
    private Integer requesterId;

    private String requesterName;

    private String requesterEmail;

    @NotBlank(message = "The title of the event is required")
    private String title;

    private String ourAttire;

    private Boolean additionalAttire;

    private String additionalDetails;

    @NotNull
    private Integer branchId;

    private String branchName;

    private String description;

    private Integer eventTypeActivityId;

    private Integer eventCampaignId;

    @NotNull
    private Integer eventCategoryId;

    @NotNull
    private Integer eventTypeId;

    private Integer eventStatusId;

    @NotNull(message = "isInternal is required")
    @Column(columnDefinition = "bit default 0")
    private boolean isInternal = false;

    @NotNull(message = "isPrivate is required")
    @Column(columnDefinition = "bit default 0")
    private boolean isPrivate = false;

    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Integer eventItineraryId;

    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Integer eventLogisticsId;

    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Integer eventPublicRelationsId;

    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Integer eventTechSupportId;

    private Boolean informationalProposal;

    private String lateSubmissionReason;
    private Integer locationId;
    private String locationName;

    private Integer partnerOrganizationId;
    private Integer universitySchoolId;
    private Boolean partnerOrganizationInvolved;
    private String partnerOrganizationWhyNot;
    private String presentationMaterials;
    private String introVideoReaction;
    private Boolean introVideoShown;

    private String purpose;

    private String otherParticipatingBranches;

    private Integer vipsTotal;
    private Integer contactsTotal;
    private Integer membersTotal;

    private String logoUrl;
    private String backgroundUrl;

    //    Event Tech Support
    private Boolean additionalAudioPerson;
    private Boolean additionalPhotoPerson;
    private Boolean additionalVideoPerson;
    private Boolean audioVisualAllowed;
    private Integer audioVisualAssignedId;
    private Boolean performances;
    private Boolean photoAllowed;
    private Integer photographerAssignedId;
    private Boolean photoVideoBefore;
    private Boolean setupHelp;
    private Boolean stageNeeded;
    private Integer tentBannerNumber;
    private Boolean tentNeeded;
    private Boolean tentUsed;
    private Boolean videoAllowed;
    private Integer videographerAssignedId;
    private String callToAction;

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

    @ManyToOne
    @JoinColumn(name = "eventCampaignId", insertable = false, updatable = false)
    private EventCampaign eventCampaign;

    @ManyToOne
    @JoinColumn(name = "eventCategoryId", insertable = false, updatable = false)
    private EventCategory eventCategory;

    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToMany
    @JoinColumn(name = "marketingMaterialId", insertable = false, updatable = false)
    @Column(insertable = false, updatable = false)
    @ToString.Exclude
    private List<MarketingMaterial> eventMarketing;

    @OneToOne
    @JoinColumn(name = "eventStatusId", insertable = false, updatable = false)
    private EventStatus eventStatus;

    @ManyToOne
    @JoinColumn(name = "locationId", insertable = false, updatable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "partnerOrganizationId", insertable = false, updatable = false)
    private Organization partnerOrganization;

    @OneToOne
    @JoinColumn(name = "universitySchoolId", insertable = false, updatable = false)
    private UniversitySchoolCode universitySchoolCode;

    @OneToMany
    @NotAudited
    @JoinColumn(name = "eventProposalId", insertable = false, updatable = false)
    @ToString.Exclude
    private List<EventFile> eventFiles;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "eventProposalId")
    private List<EventPresenter> eventPresenter;

    @Column(columnDefinition = "bit default 0", insertable = false, updatable = false)
    private boolean isPublished = false;

    @Column(insertable = false, updatable = false)
    private Integer editorId;

    @Column(insertable = false, updatable = false)
    private String editorName;

    @DateFormatConstraint
    @Column(insertable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String publishedDate;

    //      Audio/Visual fields
    @NotNull
    private String setUpEnvironment;
    @NotNull
    private String eventRoom;
    @NotNull
    private String audioDevices;
    @NotNull
    private String displayPresentation;

    //      Event Public Relations
    private Boolean media;
    private String mediaName;
    private String mediaStatus;
    private String mediaWhyNot;
    private Boolean partnerMedia;
    private Integer publicRelationsAssignedId;
    private Integer spokespersonAssignedId;
    private Integer writerAssignedId;
    private String vipIds;
    private String vipWhyNot;

    //Event Logistics
    private Boolean anotherBranchHelp;
    private BigDecimal bannerBudget;
    private String donationMessage;
    private BigDecimal foodBudget;
    private String foodDetails;
    private BigDecimal gasBudget;
    private String gasDetails;
    private String indoorOutdoor;
    private Boolean insuranceRequired;
    private BigDecimal miscBudget;
    private String miscDetails;
    private String ourItems;
    private String ourServices;
    private Boolean partnerOrganizationBudget;
    private BigDecimal partnerOrganizationBudgetDonated;
    private BigDecimal partnerOrganizationBudgetSponsor;
    private String partnerOrganizationItems;
    private String partnerOrganizationServices;
    private String registrationRedirectUrl;
    private Boolean requestNewsletterSubscription;
    private Boolean requestTShirtSize;
    private Boolean scouting;
    private BigDecimal suppliesBudget;
    private String suppliesDetails;
    private BigDecimal transportationBudget;
    private BigDecimal sumOfBudget;
    private String transportationType;

    //    Event Itinerary
    private String itineraryTime1;
    private String itineraryTime2;
    private String itineraryTime3;
    private String itineraryTime4;
    private String itineraryTime5;
    private String itineraryTime6;
    private String itineraryTime7;
    private String itineraryTime8;
    private String itineraryDetail1;
    private String itineraryDetail2;
    private String itineraryDetail3;
    private String itineraryDetail4;
    private String itineraryDetail5;
    private String itineraryDetail6;
    private String itineraryDetail7;
    private String itineraryDetail8;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventProposal that = (EventProposal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
