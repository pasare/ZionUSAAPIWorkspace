package org.zionusa.management.domain.usertype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.enums.EUserType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(
    name = "users_types",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})}
)
public class UserType implements BaseDomain<EUserType> {

    /**
     * Base Properties
     */

    @Id
    @NotNull
    @Enumerated(EnumType.STRING)
    private EUserType id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    /**
     * Properties
     */

    @NotNull
    private String name = "";

    @NotNull
    private Integer sortOrder = 10000;

    public UserType(EUserType id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserType that = (UserType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
