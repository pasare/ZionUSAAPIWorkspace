package org.zionusa.admin.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.Immutable;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(
    name = "movements_goals",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"movementId", "movementGoalTypeId", "referenceId"})}
)
public class MovementGoal implements BaseDomain<Integer> {
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

    @NotNull
    private Integer movementId;

    @NotNull
    private Integer movementGoalTypeId;

    @NotNull
    private Integer referenceId;

    private Integer referenceTypeId;

    private Integer participantCount;

    // ----------------------------------------------------
    // Preaching Goals
    // ----------------------------------------------------

    private Integer acquaintances = 0;

    private Integer contacts = 0;

    private Integer coWorkers = 0;

    private Integer family = 0;

    private Integer friends = 0;

    private Integer neighbors = 0;

    private Integer meaningful = 0;

    private Integer simple = 0;

    private Integer preachingTotal = 0;

    private Boolean usePreachingTotalOnly = false;

    // ----------------------------------------------------
    // Fruit Goals
    // ----------------------------------------------------

    private Integer evangelists = 0;

    private Integer bibleStudies = 0;

    private Integer fruit = 0;

    private Integer oneTimeAttendance = 0;

    private Integer fourTimeAttendance = 0;

    private Integer talents = 0;

    // ----------------------------------------------------
    // Activity Goals
    // ----------------------------------------------------

    private Integer activityCount = 0;

    private Integer activityBoards = 0;

    private Integer activityPoints = 0;

    // ----------------------------------------------------
    // Signature Goals
    // ----------------------------------------------------

    private Integer practiceSignatures = 0;

    private Integer gaSignatures = 0;

    private Integer readySignatures = 0;

    private Integer teachingSignatures = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MovementGoal that = (MovementGoal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "movements_goals_association_view")
    public static class Association implements BaseDomain<Integer> {

        @Id
        private Integer id;
        private Boolean archived = false;
        private Boolean hidden = false;

        private Integer referenceId;

        private String name;

        private Integer movementId;

        private String movementName;

        private String movementStartDate;

        private String movementEndDate;

        private Integer movementGoalTypeId;

        private String movementGoalTypeName;

        private Integer participantCount = 0;

        private Integer acquaintances = 0;

        private Integer activityBoards = 0;

        private Integer activityCount = 0;

        private Integer activityPoints = 0;

        private Integer bibleStudies = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer evangelists = 0;

        private Integer family = 0;

        private Integer fourTimeAttendance = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer gaSignatures = 0;

        private Integer meaningful = 0;

        private Integer oneTimeAttendance = 0;

        private Integer practiceSignatures = 0;

        private Integer neighbors = 0;

        private Integer preachingTotal = 0;

        private Integer readySignatures = 0;

        private Integer simple = 0;

        private Integer talents = 0;

        private Integer teachingSignatures = 0;

        private Boolean usePreachingTotalOnly = false;

        public Association(Integer referenceId, Integer movementId) {
            this.movementId = movementId;
            this.referenceId = referenceId;
        }
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "movements_goals_church_view")
    public static class Church implements BaseDomain<Integer> {

        @Id
        private Integer id;
        private Boolean archived = false;
        private Boolean hidden = false;

        private Integer referenceId;

        private Integer referenceTypeId;

        private String name;

        private Integer movementId;

        private String movementName;

        private String movementStartDate;

        private String movementEndDate;

        private Integer movementGoalTypeId;

        private String movementGoalTypeName;

        private Integer participantCount = 0;

        private Integer acquaintances = 0;

        private Integer activityBoards = 0;

        private Integer activityCount = 0;

        private Integer activityPoints = 0;

        private Integer bibleStudies = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer evangelists = 0;

        private Integer fourTimeAttendance = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer gaSignatures = 0;

        private Integer meaningful = 0;

        private Integer oneTimeAttendance = 0;

        private Integer practiceSignatures = 0;

        private Integer neighbors = 0;

        private Integer preachingTotal = 0;

        private Integer readySignatures = 0;

        private Integer simple = 0;

        private Integer talents = 0;

        private Integer teachingSignatures = 0;

        private Boolean usePreachingTotalOnly = false;

        public Church(Integer referenceId, Integer movementId) {
            this.movementId = movementId;
            this.referenceId = referenceId;
        }

    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "movements_goals_overseer_view")
    public static class Overseer implements BaseDomain<Integer> {

        @Id
        private Integer id;
        private Boolean archived = false;
        private Boolean hidden = false;

        private Integer referenceId;

        private String name;

        private Integer movementId;

        private String movementName;

        private String movementStartDate;

        private String movementEndDate;

        private Integer movementGoalTypeId;

        private String movementGoalTypeName;

        private Integer participantCount = 0;

        private Integer acquaintances = 0;

        private Integer activityBoards = 0;

        private Integer activityCount = 0;

        private Integer activityPoints = 0;

        private Integer bibleStudies = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer evangelists = 0;

        private Integer fourTimeAttendance = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer gaSignatures = 0;

        private Integer meaningful = 0;

        private Integer oneTimeAttendance = 0;

        private Integer practiceSignatures = 0;

        private Integer neighbors = 0;

        private Integer preachingTotal = 0;

        private Integer readySignatures = 0;

        private Integer simple = 0;

        private Integer talents = 0;

        private Integer teachingSignatures = 0;

        private Boolean usePreachingTotalOnly = false;

        public Overseer(Integer referenceId, Integer movementId) {
            this.movementId = movementId;
            this.referenceId = referenceId;
        }

    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "movements_goals_short_term_view")
    public static class ShortTermPreaching implements BaseDomain<Integer> {

        @Id
        private Integer id;
        private Boolean archived = false;
        private Boolean hidden = false;

        private Integer referenceId;

        private Integer referenceTypeId;

        private String name;

        private Integer movementId;

        private String movementName;

        private String movementStartDate;

        private String movementEndDate;

        private Integer movementGoalTypeId;

        private String movementGoalTypeName;

        private Integer participantCount = 0;

        private Integer acquaintances = 0;

        private Integer activityBoards = 0;

        private Integer activityCount = 0;

        private Integer activityPoints = 0;

        private Integer bibleStudies = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer evangelists = 0;

        private Integer fourTimeAttendance = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer gaSignatures = 0;

        private Integer meaningful = 0;

        private Integer oneTimeAttendance = 0;

        private Integer practiceSignatures = 0;

        private Integer neighbors = 0;

        private Integer preachingTotal = 0;

        private Integer readySignatures = 0;

        private Integer simple = 0;

        private Integer talents = 0;

        private Integer teachingSignatures = 0;

        private Boolean usePreachingTotalOnly = false;

        public ShortTermPreaching(Integer referenceId) {
            this.referenceId = referenceId;
        }

    }
}
