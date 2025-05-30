package org.zionusa.management.domain;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.*;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.ApplicationRoleUserPermission;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "users_application_role_user_permission_view")
public class UserApplicationRoleUserPermissionForManagement extends ApplicationRoleUserPermission {
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    private Integer userApplicationRoleId;
    private String userApplicationRoleDescription;
    private String userApplicationRoleDisplayName;
    private String userApplicationRoleName;

    private Integer userPermissionId;
    private String userPermissionDescription;
    private String userPermissionDisplayName;
    private String userPermissionName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserApplicationRoleUserPermissionForManagement that = (UserApplicationRoleUserPermissionForManagement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
