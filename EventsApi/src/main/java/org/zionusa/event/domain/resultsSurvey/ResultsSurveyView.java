package org.zionusa.event.domain.resultsSurvey;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Nationalized;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.event.domain.EventProposalTableView;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "results_survey_view")
public class ResultsSurveyView implements BaseDomain<Integer> {

    @Transient
    EventProposalTableView eventProposal;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;
    private Boolean archive; // TODO: Delete
    private String branchItems;
    private String branchServices;
    private String distributedMaterials;
    private Boolean isInternal = false;
    @Transient
    private Integer manHours = 0;
    private Integer manHoursPerVolunteer = 0;
    @Transient
    private Integer manHoursPerVolunteerCalculated;
    private Boolean mediaAttended;
    private String mediaName;
    private Integer newDonations;
    private Integer newMembersAdded;
    private Integer nonMembers;
    private String partnerOrganizationItems;
    private String partnerOrganizationServices;
    private Boolean isPrivate = false;
    private String proposedDate;
    private String proposedEndDate;
    private String proposedTime;
    private String proposedEndTime;
    private Boolean sameEventDay;
    private Boolean sameEventLocation;
    private Boolean sameEventPartnerOrganization;
    private Boolean sameNumberOfVolunteers;
    private String title;
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
    private String helpedAddresses;
    // Clean-up
    private Integer bagsOfTrash = 0;
    private Integer distanceCleanedInKm = 0;
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
    private String minBeneficiariesAgeRange;
    private String maxBeneficiariesAgeRange;
    // Event Impact Measuring
    private Boolean preSurveyCompleted;
    private Boolean postSurveyCompleted;
    private String surveyParticipants;
    private Integer surveyRespondents;
    private String environmentPopulation;


    // Branch
    private Integer branchId;
    private String branchName;

    // Event Category
    private Integer eventCategoryId;
    private String eventCategoryTitle;

    // Event Proposal
    private Integer eventProposalId;
    private Integer eventProposalBranchId;
    private String eventProposalBranchName;
    private Integer eventProposalCategoryId;
    private Boolean eventProposalIsInternal;
    private Boolean eventProposalIsPrivate;
    private Integer eventProposalLocationId;
    private Integer eventProposalPartnerOrganizationId;
    private String eventProposalProposedDate;
    private String eventProposalProposedTime;
    private String eventProposalProposedEndDate;
    private String eventProposalProposedEndTime;
    private String eventProposalTitle;
    private Integer eventProposalTypeId;

    // Event Type
    private Integer eventTypeId;
    private String eventTypeTitle;

    // Location
    private String locationAddress;
    private String locationCity;
    private String locationCountryRegion;
    private Integer locationId;
    private String locationName;
    private String locationStateProvince;
    private String locationZipPostalCode;

    // Partner Organization
    private String partnerOrganizationAddress;
    private String partnerOrganizationCity;
    private String partnerOrganizationCountryRegion;
    private Integer partnerOrganizationId;
    private String partnerOrganizationStateProvince;
    private String partnerOrganizationTitle;
    private String partnerOrganizationZipPostalCode;

    // Workflow
    private Boolean finalized;
    private String workflowStatus;

    // Media
    private Boolean bannerGroupPhoto;
    private Boolean eventVideo;
    private Boolean fragrancesRecorded;
    private String noEventVideoReason;
    private String noVipPhotoReason;
    private Boolean vipPhotos;

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
    private String writeUp;

    @Nationalized
    @Column(columnDefinition = "TEXT")
    private String fragrances;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResultsSurveyView that = (ResultsSurveyView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
