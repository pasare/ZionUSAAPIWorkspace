package org.zionusa.management.domain.users;

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
@Table(name = "users_titles")
public class UserTitle implements BaseDomain<Integer> {

    /**
     * Base Properties
     */

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

    /**
     * Properties
     */

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Column(unique = true)
    private String displayName;

    @NotNull
    private short sortOrder = 10000;

    public UserTitle(Integer id, String name, String displayName) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserTitle userTitle = (UserTitle) o;
        return Objects.equals(id, userTitle.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
