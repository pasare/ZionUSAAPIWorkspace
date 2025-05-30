package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.admin.domain.activities.Activity;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;
import org.zionusa.base.util.constraints.DateTimeFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "challenges")
public class Challenge implements BaseDomain<Integer> {

    @ManyToMany(fetch = FetchType.EAGER)
    List<Activity> activities;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean archived = false;
    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean hidden = false;
    private Integer requesterId;
    @Column(name = "category_id")
    private Integer categoryId;
    private Integer churchId;
    private String churchName;
    private Integer groupId;
    private Integer teamId;
    private Integer movementId;
    @NotNull
    private String name;
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
    private String activityDisplayField;
    private String logoUrl;
    private String backgroundUrl;
    private String link;
    private String notes;
    private double points = 0;
    private double pointsMultiplier = 1;
    private String requesterName;
    private String requesterImageUrl;
    @Column(columnDefinition = "char(1) default ''")
    private Character type;
    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date = "2020-01-01";
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String expirationDate;
    @DateTimeFormatConstraint
    @Column(insertable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String notificationDateAndTime;
    private String feedback;
    private String attachmentUrl;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private ChallengeCategory category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Challenge challenge = (Challenge) o;
        return Objects.equals(id, challenge.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
