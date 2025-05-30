package org.zionusa.management.domain.access;

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

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "accesses")
public class Access implements BaseDomain<Integer> {

    /**
     * Base Properties
     */

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "archived", columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    /**
     * Properties
     */

    @NotNull
    @Column(unique = true)
    private String name = "";

    @NotNull
    private Integer sortOrder = 10000;

    public Access(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.archived = false;
        this.hidden = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Access access = (Access) o;
        return Objects.equals(id, access.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
