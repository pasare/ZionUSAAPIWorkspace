package org.zionusa.admin.domain.activities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.admin.util.constraints.DateFormatConstraint;
import org.zionusa.admin.util.constraints.TimeFormatConstraint;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "activities_group_plans", uniqueConstraints = {@UniqueConstraint(columnNames = {"groupId", "date"})})
public class GroupActivityPlan implements BaseDomain<Integer> {
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

    private Integer churchId;

    @DateFormatConstraint
    private String date;

    @TimeFormatConstraint
    private String openTime;

    @TimeFormatConstraint
    private String closeTime;

    private Integer groupId;

    private String notes;

    private Boolean submitted;

    private Integer participantCount;

    private Integer gospelWorkerCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupActivityPlan that = (GroupActivityPlan) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
