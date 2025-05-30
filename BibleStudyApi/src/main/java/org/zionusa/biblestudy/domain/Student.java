package org.zionusa.biblestudy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "students")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "archived", columnDefinition = "bit default 0")
    private boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    private String city;

    private Integer churchId;

    private String churchName;

    private Integer contactId;

    private String gender;

    private String firstName;

    private String middleName;

    private String lastName;

    private Boolean lostSheep;

    private String ageRange;

    private String ethnicity;

    private String language;

    private Integer studiesCompleted;

    private Integer studiesPercentage;

    private Integer lastStudyId;

    private String lastStudyName;

    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String lastStudyDate;

    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String baptismDate;

    private Integer baptismStudyId;

    private String pictureUrl;

    @Column(columnDefinition = "varchar(25) not null default ''")
    private String relationshipType;

    private Integer stateId;

    private String stateName;

    private Integer userId1;

    private String userDisplayName1;

    private Integer userId2;

    private String userDisplayName2;

    private Integer userId3;

    private String userDisplayName3;

    /**
     * Steps for a Bible study student
     * -------------------------------
     * 0. Prayer
     * 1. Develop a relationship
     * 2. Introduce the church
     * 3. Study all of level 1
     * 4. Visit their home/church
     * 5. Baptism
     * 6. 1 time attendance
     * 7. 4 time attendance
     * 8. Evangelist
     */

    @DateFormatConstraint()
    private String developRelationship; // Step 1

    @DateFormatConstraint()
    private String introduceChurch; // Step 2

    // Step 3 - Study Lvl 1

    @DateFormatConstraint()
    private String visitChurch; // Step 4

    @DateFormatConstraint()
    private String visitHome; // Step 4
    @DateFormatConstraint()
    private String oneTimeAttendance; // Step 6
    @DateFormatConstraint()
    private String fourTimeAttendance; // Step 7
    @DateFormatConstraint()
    private String becomeEvangelist; // Step 8
    private Date createdDate;
    private Date updatedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Data
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @NoArgsConstructor
    public static class BaptismDetails { // Step 5 - Baptism
        @EqualsAndHashCode.Include
        private Integer id;

        @DateFormatConstraint()
        private String baptismDate;

        private Integer baptismStudyId;
    }

    @Data
    @Entity
    @Immutable
    @Table(name = "lost_sheep_view")
    public static class LostSheepReport {
        @Id
        private Integer churchId;
        private String churchName;
        private Integer numOfLostSheep;
        private Integer numOfContacted;
        private Integer numOfVisited;
        private Integer numOfOneTimer;
        private Integer numOfFourTimer;
        private Integer bibleStudyCount;
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "lost_sheep_student_view")
    public static class LostSheep {
        @Id
        @EqualsAndHashCode.Include
        private Integer id;
        private String baptismDate;
        private Integer churchId;
        private String churchName;
        private String city;
        private String developRelationship;
        private String firstName;
        private String fourTimeAttendance;
        private String gender;
        private String lastName;
        private String middleName;
        private String oneTimeAttendance;
        private String pictureUrl;
        private String relationshipType;
        private String stateName;
        private Integer studiesCompleted;
        private String visitChurch;
        private String visitHome;
    }
}
