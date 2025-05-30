package org.zionusa.admin.domain.activities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
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
@Table(name = "activities")
public class Activity implements BaseDomain<Integer> {

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

    private String abbreviation;

    private String backgroundColor;

    private String backgroundUrl;

    private Integer requesterId;

    @Column(name = "category_id")
    private Integer categoryId;

    private Integer churchId;

    private String churchName;

    private Integer groupId;

    private String iconUrl;

    private Integer teamId;

    private Integer objectId;

    @NotNull
    private String name;

    @Column(columnDefinition = "varchar(655) default ''")
    private String description;

    private String logoUrl;

    private String link;

    private String notes;

    private String feedback;

    private String requesterName;

    private String requesterImageUrl;

    private String tag;

    private String textColor;

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

    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date = "2020-01-01";

    @DateTimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String notificationDateAndTime;

    @NotNull
    private double points = 0;

    private String attachmentUrl;

    private Boolean useForNotifications;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private ActivityCategory category;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "activityId", insertable = false, updatable = false)
    @ToString.Exclude
    private List<ActivityLog> activityLogs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
