package org.zionusa.event.domain.resultsSurvey;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Nationalized;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;
import org.zionusa.base.util.constraints.TimeFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@AllArgsConstructor
@Table(name = "results_survey")
public class ResultsSurvey implements BaseDomain<Integer> {

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

    private Boolean archive; // TODO: Delete

    // Branch
    private Integer branchId;

    // Event Proposal
    private Integer eventCategoryId;
    private Integer eventProposalId;
    private Integer eventTypeId;
    private Boolean isInternal = false;
    private Boolean isPrivate = false;
    private String title;

    // Date and Time
    private Boolean sameEventDay;
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

    // Details
    private String branchItems;
    private String branchServices;
    private String distributedMaterials;
    private String partnerOrganizationItems;
    private String partnerOrganizationServices;
    private Boolean mediaAttended;
    private String mediaName;
    private Integer newDonations;

    // Location
    private Boolean sameEventLocation;
    private Integer locationId;

    // Organization
    private Boolean sameEventPartnerOrganization;
    private Integer partnerOrganizationId;

    // Volunteers
    private Boolean sameNumberOfVolunteers;
    private Integer manHoursPerVolunteer;
    private Integer newMembersAdded;
    private Integer nonMembers;
    private Integer volunteerFemaleAdults = 0;
    private Integer volunteerMaleAdults = 0;
    private Integer volunteerFemaleCollegeStudents = 0;
    private Integer volunteerMaleCollegeStudents = 0;
    private Integer volunteerFemaleYoungAdults = 0;
    private Integer volunteerMaleYoungAdults = 0;
    private Integer volunteerFemaleTeenagers = 0;
    private Integer volunteerMaleTeenagers = 0;

    // Event Results
    // Tabling
    private Integer visitors = 0;
    // Disaster Recovery/ Relief
    private Integer homesHelped = 0;
    private Integer peopleHelped = 0;
    private String HelpedAddresses;
    // Clean-up
    private Integer bagsOfTrash = 0;
    private double distanceCleanedInKm = 0;
    private Integer distanceCleanedInMiles = 0;
    private Integer weightOfTrashCleaned = 0;
    // Tree Planting
    private Integer treesPlanted = 0;
    private String typeOfTrees;

    // Blood Drive
    private Integer pintsBloodCollected = 0;
    private Integer ounceBloodCollected = 0;
    private Integer bloodDonors = 0;
    private Integer volunteerBloodDonors = 0;
    private Integer guestBloodDonors = 0;
    private Integer numberOfLivesSaved = 0;
    private Integer maleAPositiveBlood = 0;
    private Integer maleANegativeBlood = 0;
    private Integer maleBPositiveBlood = 0;
    private Integer maleBNegativeBlood = 0;
    private Integer maleOPositiveBlood = 0;
    private Integer maleONegativeBlood = 0;
    private Integer maleABPositiveBlood = 0;
    private Integer maleABBlood = 0;
    private Integer femaleAPositiveBlood = 0;
    private Integer femaleANegativeBlood = 0;
    private Integer femaleBPositiveBlood = 0;
    private Integer femaleBNegativeBlood = 0;
    private Integer femaleOPositiveBlood = 0;
    private Integer femaleONegativeBlood = 0;
    private Integer femaleABPositiveBlood = 0;
    private Integer femaleABBlood = 0;

    // Book DRive
    private String beneficiariesAgeRange;
    private Integer minBeneficiariesAgeRange;
    private Integer maxBeneficiariesAgeRange;

    // Event Impact Measuring
    private Boolean preSurveyCompleted;
    private Boolean postSurveyCompleted;
    private String surveyParticipants;
    private Integer surveyRespondents;
    private String environmentPopulation;


    // Workflow
    private Boolean finalized;
    private String workflowStatus;

    // Event Media
    private Boolean bannerGroupPhoto; // TODO: Move
    private Boolean eventVideo; // TODO: Move
    private String noEventVideoReason; // TODO: Move
    private Boolean fragrancesRecorded; // TODO: Move
    private Boolean vipPhotos; // TODO: Move
    private String noVipPhotoReason; // TODO: Move

    //30 Day Post Event Update
    private Integer guestStillStudying;
    private Integer guestBaptized;
    //Presenter Ratings
    private Integer wellSpokenRating;
    private Integer presentationRating;
    private Integer engagementRating;
    private Boolean presentAgain;

    //    Event Donations
    private BigDecimal donation1Amount;
    private String donation1Item;
    private BigDecimal donation2Amount;
    private String donation2Item;
    private BigDecimal donation3Amount;
    private String donation3Item;
    private BigDecimal donation4Amount;
    private String donation4Item;
    private BigDecimal donation5Amount;
    private String donation5Item;
    private BigDecimal donation6Amount;
    private String donation6Item;
    private BigDecimal totalDonationAmount;

    @Nationalized
    @Column(columnDefinition = "TEXT")
    private String writeUp; // TODO: Move

    @Nationalized
    @Column(columnDefinition = "TEXT")
    private String fragrances; // TODO: Move

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResultsSurvey that = (ResultsSurvey) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
