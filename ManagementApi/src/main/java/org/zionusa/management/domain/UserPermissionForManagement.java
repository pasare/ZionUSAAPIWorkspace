package org.zionusa.management.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.UserPermission;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@Table(name = "user_permissions")
public class UserPermissionForManagement extends UserPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    @NotNull
    @Column(name = "name", unique = true)
    private String name;

    @NotNull
    private String displayName;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPermissionForManagement that = (UserPermissionForManagement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
