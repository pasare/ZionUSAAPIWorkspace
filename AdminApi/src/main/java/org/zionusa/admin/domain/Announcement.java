package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;
import org.zionusa.base.util.constraints.DateTimeFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "announcements")
public class Announcement implements BaseDomain<Integer> {
    public static final String GENERAL_ANNOUNCEMENT_ADDED_SUB_SOURCE = "general-added";
    public static final String FRAGRANCE_ANNOUNCEMENT_ADDED_SUB_SOURCE = "fragrance-added";
    public static final String GENERAL_ANNOUNCEMENT_APPROVED_SUB_SOURCE = "general-approved";
    public static final String FRAGRANCE_ANNOUNCEMENT_APPROVED_SUB_SOURCE = "fragrance-approved";
    public static final String GENERAL_ANNOUNCEMENT_NEW_SUB_SOURCE = "general-new";
    public static final String FRAGRANCE_ANNOUNCEMENT_NEW_SUB_SOURCE = "fragrance-new";
    public static final String ACTIVITY_ADDED_SUB_SOURCE = "activity-added";
    public static final String ACTIVITY_APPROVED_SUB_SOURCE = "activity-approved";
    public static final String ACTIVITY_NEW_SUB_SOURCE = "activity-new";
    public static final String ACTIVITY_PUBLISHED_SUB_SOURCE = "activity-published";
    public static final String CHALLENGE_ADDED_SUB_SOURCE = "challenge-added";
    public static final String CHALLENGE_APPROVED_SUB_SOURCE = "challenge-approved";
    public static final String CHALLENGE_NEW_SUB_SOURCE = "challenge-new";
    public static final String GOAL_ADDED_SUB_SOURCE = "challenge-added";
    public static final String ASSOCIATION_GOAL_ADDED_SUB_SOURCE = "challenge-association-added";
    public static final String CHURCH_GOAL_ADDED_SUB_SOURCE = "challenge-church-added";
    public static final String GROUP_GOAL_ADDED_SUB_SOURCE = "challenge-group-added";
    public static final String TEAM_GOAL_ADDED_SUB_SOURCE = "challenge-team-added";
    public static final String GOAL_APPROVED_SUB_SOURCE = "challenge-approved";
    public static final String GOAL_NEW_SUB_SOURCE = "challenge-new";
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typeId", insertable = false, updatable = false)
    AnnouncementType type;
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
    private Integer requesterId;
    private Integer churchId;
    private String churchName;
    private Integer groupId;
    private Integer teamId;
    private String videoUrl;
    @NotNull
    private Integer typeId;
    @NotNull
    @Nationalized
    private String title;
    @Nationalized
    private String subTitle;
    @Nationalized
    private String requesterName;
    private String requesterImageUrl;
    private String reviewerName;
    @Nationalized
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(insertable = false, updatable = false)
    private Boolean approved;
    @Column(insertable = false, updatable = false)
    private Integer approverId;
    @Column(insertable = false, updatable = false)
    private String approverName;
    @Column(columnDefinition = "bit default 0", insertable = false, updatable = false)
    private boolean published;
    @Column(insertable = false, updatable = false)
    private Integer editorId;
    @Column(insertable = false, updatable = false)
    private String editorName;
    @DateFormatConstraint
    @Column(insertable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String publishedDate;
    @DateTimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String notificationDateAndTime;
    @Nationalized
    @Column(insertable = false, updatable = false)
    private String feedback;
    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;
    private String requiredResponse;
    private String attachmentUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Announcement that = (Announcement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
