package org.zionusa.biblestudy.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.domain.Member;
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
@Table(name = "SIGNATURES", uniqueConstraints = {@UniqueConstraint(columnNames = {"teacher_id", "study_id"})})
public class Signature implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "archived", columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    @Column(name = "teacher_id", columnDefinition = "int not null")
    private Integer teacherId;

    // TODO: Implement @NotNull after Oct 1 Release
    private String teacherName;

    private String teacherChurchName;

    @Column(name = "study_id", columnDefinition = "int not null")
    private Integer studyId;

    @Column(columnDefinition = "tinyint not null default 0")
    private Integer generalAssembly;

    private Integer generalAssemblyGraderId;

    private String generalAssemblyGraderName;

    @DateFormatConstraint()
    @Column(columnDefinition = "varchar(12)")
    private String generalAssemblyUpdatedDate;

    @Column(columnDefinition = "tinyint not null default 0")
    private Integer ready;

    private Integer readyGraderId;

    private String readyGraderName;

    @DateFormatConstraint()
    @Column(columnDefinition = "varchar(12)")
    private String readyUpdatedDate;

    @Column(columnDefinition = "tinyint not null default 0")
    private Integer teaching;

    private Integer teachingGraderId;

    private String teachingGraderName;

    @DateFormatConstraint()
    @Column(columnDefinition = "varchar(12)")
    private String teachingUpdatedDate;

    private Date createdDate;

    private Date updatedDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "study_id", insertable = false, updatable = false)
    private Study study;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Signature signature = (Signature) o;
        return Objects.equals(id, signature.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Data
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @NoArgsConstructor
    public static class SignatureCount {
        @EqualsAndHashCode.Include
        private Integer teacherId;
        private int generalAssembly;
        private int ready;
        private int teaching;

        public SignatureCount(Integer teacherId, int generalAssembly, int ready, int teaching) {
            this.teacherId = teacherId;
            this.generalAssembly = generalAssembly;
            this.ready = ready;
            this.teaching = teaching;
        }
    }

    @Data
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @NoArgsConstructor
    public static class SignatureDisplay {
        @EqualsAndHashCode.Include
        private Integer id;
        private Integer studyId;
        private String studyName;
        private Integer teacherId;
        private String teacherName;
        private Integer teacherChurchId;
        private String teacherChurchName;
        private String teacherGender;
        private String teacherGroupName;
        private String teacherPictureUrl;
        private String teacherTeamName;
        private Boolean generalAssembly;
        private Boolean ready;
        private Boolean teaching;

        public SignatureDisplay(Signature signature, Member member) {
            this.id = signature.getId();
            this.studyId = signature.getStudyId();
            this.studyName = signature.getStudy().getTitle();
            this.teacherId = signature.getTeacherId();
            this.teacherName = signature.getTeacherName();
            this.teacherChurchId = member.getChurchId();
            this.teacherChurchName = member.getChurchName();
            this.teacherGender = member.getGender();
            this.teacherGroupName = member.getGroupName();
            this.teacherPictureUrl = member.getPictureUrl();
            this.teacherTeamName = member.getTeamName();
            this.generalAssembly = signature.getGeneralAssembly() != null && signature.getGeneralAssembly() > 0;
            this.ready = signature.getReady() != null && signature.getReady() > 0;
            this.teaching = signature.getTeaching() != null && signature.getTeaching() > 0;
        }
    }

    @Data
    @Immutable
    public static class SignatureEvaluation {

        public String gradeType;
        @DateTimeFormat
        public String graderDate;
        public Integer graderId;
        public String graderName;
        public Integer studyId;
        public Integer teacherId;
        public String teacherName;
        public String teacherChurchName;
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "signatures_grader_view")
    public static class SignatureGrader {

        @Id
        @EqualsAndHashCode.Include
        private String id;
        private Integer bookNumber;
        private Integer chapterNumber;
        private String gradeType;
        private String graderDate;
        private Integer graderId;
        private String graderName;
        private Integer signatureId;
        private Integer studyId;
        private Integer teacherId;
        private String teacherName;
        private String teacherChurchName;
        private String title;

    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "signatures_general_assembly_completed_movement_view")
    public static class SignatureGeneralAssemblyMovement {

        @Id
        @EqualsAndHashCode.Include
        private Integer teacherId;
        private Integer levelOneCompleted;
        private Integer levelTwoCompleted;
        private Integer levelThreeCompleted;
        private Integer levelFourCompleted;
        private Integer levelFiveCompleted;
        private Integer staffOneCompleted;
        private Integer totalCompleted;
        private Integer totalCount;
    }

    @Data
    @Immutable
    public static class SignatureGeneralAssemblyMovementReport {

        private Integer churchId;
        private String churchName;
        private Integer groupId;
        private String groupName;
        private Integer levelOneCompleted = 0;
        private Integer levelTwoCompleted = 0;
        private Integer levelThreeCompleted = 0;
        private Integer levelFourCompleted = 0;
        private Integer levelFiveCompleted = 0;
        private Integer staffOneCompleted = 0;
        private Integer teacherId;
        private String teacherDisplayName;
        private Integer teamId;
        private String teamName;
        private Integer totalCompleted = 0;
        private Integer totalCount = 0;

        public SignatureGeneralAssemblyMovementReport(Signature.SignatureGeneralAssemblyMovement signatureGeneralAssemblyMovement, Member member) {
            this.churchId = member.getChurchId();
            this.churchName = member.getChurchName();
            this.groupId = member.getGroupId();
            this.groupName = member.getGroupName();
            this.teacherDisplayName = member.getDisplayName();
            this.teacherId = member.getId();
            this.teamId = member.getTeamId();
            this.teamName = member.getTeamName();
            if (signatureGeneralAssemblyMovement != null) {
                this.levelOneCompleted = signatureGeneralAssemblyMovement.getLevelOneCompleted();
                this.levelTwoCompleted = signatureGeneralAssemblyMovement.getLevelTwoCompleted();
                this.levelThreeCompleted = signatureGeneralAssemblyMovement.getLevelThreeCompleted();
                this.levelFourCompleted = signatureGeneralAssemblyMovement.getLevelFourCompleted();
                this.levelFiveCompleted = signatureGeneralAssemblyMovement.getLevelFiveCompleted();
                this.staffOneCompleted = signatureGeneralAssemblyMovement.getStaffOneCompleted();
                this.totalCompleted = signatureGeneralAssemblyMovement.getTotalCompleted();
                this.totalCount = signatureGeneralAssemblyMovement.getTotalCount();
            }
        }
    }
}
