package org.zionusa.management.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Deprecated
@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "church_types")
public class ChurchType implements BaseDomain<Integer> {
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
    private String name;

    @NotNull
    private short sortOrder;

    public ChurchType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChurchType that = (ChurchType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
