package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.Immutable;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "movement_user_level_view")
public class MovementUserLevel implements BaseDomain<String> {
    @Id
    private String id;
    private Boolean archived = false;
    private Boolean hidden = false;

    private Integer movementId;

    private Integer level;

    private Integer totalCount;

    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MovementUserLevel that = (MovementUserLevel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
