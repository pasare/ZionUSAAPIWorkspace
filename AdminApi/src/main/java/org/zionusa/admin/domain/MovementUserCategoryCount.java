package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.Immutable;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
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
@Table(name = "movement_user_category_count_view")
public class MovementUserCategoryCount implements BaseDomain<Integer> {

    @Id
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    private int attendanceCount;

    private int errorCount; // Counts activities recorded that shouldn't have the movement_id

    private int fruitCount;

    private int preachingCount;

    private int signatureCount;

    @Column(name = "t_signature_count")
    private int tSignatureCount;

    private int totalCount;

    private int totalPoints;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MovementUserCategoryCount that = (MovementUserCategoryCount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
