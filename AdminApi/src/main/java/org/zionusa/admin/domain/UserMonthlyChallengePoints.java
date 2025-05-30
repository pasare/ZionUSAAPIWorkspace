package org.zionusa.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_monthly_challenge_points")
public class UserMonthlyChallengePoints implements BaseDomain<Integer> {

    @JsonIgnore
    private Integer id;

    @Id
    @EqualsAndHashCode.Include
    private int userId;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    private double points;

    @EqualsAndHashCode.Include
    private int month;

    @EqualsAndHashCode.Include
    private int year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserMonthlyChallengePoints that = (UserMonthlyChallengePoints) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
