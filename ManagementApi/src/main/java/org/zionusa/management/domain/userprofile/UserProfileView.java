package org.zionusa.management.domain.userprofile;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "users_profile_view")
public class UserProfileView implements BaseDomain<Integer> {

    /**
     * Base Properties
     */

    @Id
    @Column(name = "id")
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    /**
     * Properties
     */

    private String address;
    private String bachelorsDegree;
    private String bachelorsSchool;
    private Integer baptismMonth;
    private Integer baptismYear;
    private Integer birthMonth;
    private Integer birthYear;
    private String canGiveSignature;
    private String certifications;
    // TODO: Migrate to boolean
    private int children;
    private String company;
    private String companyCity;
    private String city;
    private String currentJobDuration;
    private String desiredField;
    private String displayName;
    private String doctorateDegree;
    private String doctorateSchool;
    private Boolean enabled = true;
    private String ethnicity;
    // TODO: Move to different table
    private String faithLevel;
    // TODO: Move to different table
    private Boolean flag;
    private String firstName;
    private Boolean gaGrader;
    private String gender;
    private String gospelAspirations;
    private String gospelPlan;
    // TODO: Move to different table
    private Boolean gospelWorker;
    private String hobbies;
    private String iba;
    private String ibaOrganization;
    private String jobTitle;
    private String lastLoginDate;
    private String lastName;
    private Boolean married;
    private String mastersDegree;
    private String mastersSchool;
    private String middleName;
    private String occupation;
    private Integer parentBranchId;
    private String professionalLicenses;
    private Boolean readyGrader;
    private Boolean shortTermParticipant;
    private Boolean teacher;
    private Boolean theologicalStudent;
    private String typeOfBusiness;
    private String university;
    private String universityPosition;
    private String username;
    private Boolean worksRemotely;

    /**
     * Relationships
     */

    private Integer associationId;
    private String associationName;

    private Integer branchId;
    private String branchName;

    private Integer groupId;
    private String groupName;

    private Integer mainBranchId;
    private String mainBranchName;

    private Integer pictureId;

    private Integer regionId;
    private String regionName;

    private Integer spouseId;
    private Integer spouseName;
    private Integer spousePictureUrl;
    private Integer spouseThumbnail;

    private Integer userId;
    private String userDisplayName;

    private Integer teamId;
    private String teamName;

    private Integer titleId;
    private String titleName;

    private String typeId;
    private String typeName;

//    /**
//     * Transient
//     */
//
//    @Transient
//    private List<UserProfileInformation> churchActivities = new ArrayList<>(); // 1
//
//    @Transient
//    private List<UserProfileInformation> churchPositions = new ArrayList<>(); // 2
//
//    @Transient
//    private List<UserProfileInformation> serviceDuties = new ArrayList<>(); // 3
//
//    @Transient
//    private List<UserProfileInformation> professionalSkills = new ArrayList<>(); // 4
//
//    @Transient
//    private List<UserProfileInformation> technicalSkills = new ArrayList<>(); // 5
//
//    @Transient
//    private List<UserProfileInformation> talents = new ArrayList<>(); // 6
//
//    @Transient
//    private List<UserProfileInformation> tithes = new ArrayList<>(); // 7
//
//    @Transient
//    private List<UserProfileInformation> incidentReports = new ArrayList<>(); // 8
//
//    @Transient
//    private List<UserProfileInformation> previousChurches = new ArrayList<>(); // 10
//
//    @Transient
//    private List<UserProfileInformation> languages = new ArrayList<>(); // 11
//
//    @Transient
//    private List<UserProfileInformation> shortTermPreaching = new ArrayList<>(); // 12
}
