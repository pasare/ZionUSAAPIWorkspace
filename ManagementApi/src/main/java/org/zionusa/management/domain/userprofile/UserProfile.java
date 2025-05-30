package org.zionusa.management.domain.userprofile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.management.domain.UserProfileInformation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_profiles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserProfile implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", unique = true)
    private Integer userId;

    @Column(name = "church_id")
    private Integer churchId;

    private String address;

    private String city;

    private String faithLevel;

    private String gospelAspirations;

    private String gospelPlan;

    private String iba;

    private String ibaOrganization;

    private String company;

    private String companyCity;

    private Integer companyStateId;

    private String currentJobDuration;

    private String certifications;

    private String desiredField;

    private Boolean flag;

    private String jobTitle;

    private String occupation;

    private String professionalLicenses;

    private String typeOfBusiness;

    private String bachelorsDegree;

    private String bachelorsSchool;

    private String canGiveSignature;

    private String mastersDegree;

    private String mastersSchool;

    private String doctorateDegree;

    private String doctorateSchool;

    private Integer stateId;

    private String university;

    private String universityPosition;

    private int children;

    private String ethnicity;

    private String hobbies;

    private Boolean shortTermParticipant;

    private Boolean worksRemotely;

    @Transient
    private List<UserProfileInformation> churchActivities = new ArrayList<>(); // 1

    @Transient
    private List<UserProfileInformation> churchPositions = new ArrayList<>(); // 2

    @Transient
    private List<UserProfileInformation> serviceDuties = new ArrayList<>(); // 3

    @Transient
    private List<UserProfileInformation> professionalSkills = new ArrayList<>(); // 4

    @Transient
    private List<UserProfileInformation> technicalSkills = new ArrayList<>(); // 5

    @Transient
    private List<UserProfileInformation> talents = new ArrayList<>(); // 6

    @Transient
    private List<UserProfileInformation> tithes = new ArrayList<>(); // 7

    @Transient
    private List<UserProfileInformation> incidentReports = new ArrayList<>(); // 8

    @Transient
    private List<UserProfileInformation> previousChurches = new ArrayList<>(); // 10

    @Transient
    private List<UserProfileInformation> languages = new ArrayList<>(); // 11

    @Transient
    private List<UserProfileInformation> shortTermPreaching = new ArrayList<>(); // 11

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
