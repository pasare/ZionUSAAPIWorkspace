package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "group_monthly_challenge_points")
public class GroupMonthlyChallengePoints implements BaseDomain<Integer> {

    @Transient
    private Integer id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    @Id
    private int groupId;

    private double points;

    private int month;

    private int year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupMonthlyChallengePoints that = (GroupMonthlyChallengePoints) o;
        return Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
