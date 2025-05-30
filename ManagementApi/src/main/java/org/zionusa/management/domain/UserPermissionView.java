package org.zionusa.management.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "users_permissions_materialized_view")
public class UserPermissionView implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String viewId;

    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Integer id;

    private Boolean archived = false;
    private Boolean hidden = false;
    private Integer accessId;
    private String accessName;
    private Integer applicationRoleId;
    private String applicationRoleDisplayName;
    private String applicationRoleName;
    private String displayName;
    private Boolean enabled = true;
    private Boolean gaGrader;
    private String gender;
    private Integer pictureId;
    private String lastLoginDate;
    private Boolean readyGrader;
    private Integer roleId;
    private String roleName;
    private Boolean teacher;
    private Integer teamId;
    private Integer userId;
    private Boolean userName;
    private Integer userPermissionId;
    private String userPermissionDisplayName;
    private String userPermissionName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPermissionView that = (UserPermissionView) o;
        return Objects.equals(viewId, that.viewId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
