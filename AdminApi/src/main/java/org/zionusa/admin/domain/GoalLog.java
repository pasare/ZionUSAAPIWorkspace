package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.admin.domain.activities.ActivityLog;
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
@Table(name = "goal_logs")
public class GoalLog implements BaseDomain<Integer> {

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
    private Integer userId;

    @NotNull
    private Integer challengeId;

    private boolean completed = false;

    @Deprecated
    private boolean allGroup = false;

    private Integer shareReferenceId;

    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String startDate;

    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String endDate;

    @DateTimeFormatConstraint
    @Column(insertable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String notificationDateAndTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challengeId", insertable = false, updatable = false)
    private Challenge challenge;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "goalLogId", insertable = false, updatable = false)
    @ToString.Exclude
    private List<ActivityLog> completedActivities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GoalLog goalLog = (GoalLog) o;
        return Objects.equals(id, goalLog.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
