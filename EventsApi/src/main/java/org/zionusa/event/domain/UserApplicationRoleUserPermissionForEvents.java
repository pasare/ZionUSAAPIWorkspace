package org.zionusa.event.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.ApplicationRoleUserPermission;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "users_application_role_user_permission_view")
public class UserApplicationRoleUserPermissionForEvents extends ApplicationRoleUserPermission {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean archived = false;
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
        UserApplicationRoleUserPermissionForEvents that = (UserApplicationRoleUserPermissionForEvents) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
