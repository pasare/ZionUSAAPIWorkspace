package org.zionusa.admin.domain.activities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "activity_logs")
public class ActivityLog implements BaseDomain<Integer> {
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activityId", insertable = false, updatable = false)
    Activity activity;
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
    private Integer churchId;
    private Integer groupId;
    private Integer teamId;
    private String userName;
    private String teamName;
    private String groupName;
    private String churchName;
    @NotNull
    private Integer activityId;
    private String name;
    private String description;
    private String notes;
    private Integer challengeLogId;
    private Integer goalLogId;
    private Integer movementId;
    private Integer objectId;
    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ActivityLog that = (ActivityLog) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
