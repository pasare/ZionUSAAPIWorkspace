package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission implements BaseDomain<Integer> {

    Reference reference;
    int referenceId;
    Access access;
    OrganizationLevel organizationLevel;
    String organizationLevelIds;
    String userIds;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public enum Reference {
        APPLICATION,
        FORM,
        REPORT
    }

    public enum Access {
        ADMIN,
        OVERSEER,
        CHURCHLEADER,
        GROUPLEADER,
        TEAMLEADER,
        MEMBER,
    }

    public enum OrganizationLevel {
        CHURCH,
        GROUP,
        TEAM,
        USER
    }
}
