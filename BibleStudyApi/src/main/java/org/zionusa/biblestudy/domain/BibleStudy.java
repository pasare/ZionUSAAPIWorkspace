package org.zionusa.biblestudy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "bible_studies")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class BibleStudy implements BaseDomain<Integer> {

    public static final String BIBLE_STUDY_ADDED_SUB_SOURCE = "bible-study-added";
    public static final String BIBLE_STUDY_APPROVED_SUB_SOURCE = "bible-study-approved";
    public static final String BIBLE_STUDY_TEACHER_ASSIGNED_SUB_SOURCE = "bible-study-teacher";
    public static final String BIBLE_STUDY_TEACHER_AVAILABLE_SUB_SOURCE = "bible-study-teacher-available";

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

    private Integer requesterId;

    private String requesterName;

    @Column(name = "student_id")
    private Integer studentId;

    private String studentName;

    private Integer teacherId;

    private String teacherName;

    private Integer parentChurchId;

    private String parentChurchName;

    private Integer churchId;

    private String churchName;

    private Integer groupId;

    private Integer teamId;

    @Column(name = "study_id")
    private Integer studyId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;

    private String time;

    private String location;

    @Deprecated
    private boolean attended;

    @Deprecated
    private boolean approved;

    @Deprecated
    @Column(columnDefinition = "bit default 0")
    private boolean denied;

    private Boolean teacherAvailable;

    private Integer studyRating;

    private String notes;

    @Deprecated
    private String preacherName;

    private String invitedBy;

    private Date createdDate;

    private Date updatedDate;

    @Transient
    private String metadata;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "study_id", insertable = false, updatable = false)
    private Study study;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bible_study_id", insertable = false, updatable = false)
    private BibleStudyProgress progress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BibleStudy that = (BibleStudy) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
