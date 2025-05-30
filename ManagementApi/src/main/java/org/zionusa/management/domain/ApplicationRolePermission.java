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

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@Table(name = "application_role_permissions")
public class ApplicationRolePermission implements BaseDomain<Integer> {

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

    private Integer applicationRoleId;

    @OneToOne
    @NotNull(message = "At least one application role must be assigned")
    @JoinColumn(name = "applicationRoleId", insertable = false, updatable = false)
    private ApplicationRole applicationRole;

    private Integer permissionId;

    @OneToOne
    @NotNull(message = "At least one permission must be assigned")
    @JoinColumn(name = "permissionId", insertable = false, updatable = false)
    private UserPermissionForManagement permission;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationRolePermission that = (ApplicationRolePermission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
