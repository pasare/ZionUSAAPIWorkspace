package org.zionusa.admin.domain;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "seal_movement_view")
public class SealMovement implements BaseDomain<Integer> {

    @Transient
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    @Id
    private Integer userId;

    private Integer challengeId;

    private String challengeName;

    private String completedDate;

    private Boolean completed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SealMovement that = (SealMovement) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
