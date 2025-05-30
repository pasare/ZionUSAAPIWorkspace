package org.zionusa.management.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@AllArgsConstructor
@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_application_roles")
public class UserApplicationRole extends Auditable<String> {
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "The user Id is required")
    private Integer userId;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User.DisplayUser user;

    private Integer applicationRoleId;

    @OneToOne
    @NotNull(message = "At least one application role must be assigned")
    @JoinColumn(name = "applicationRoleId", insertable = false, updatable = false)
    private ApplicationRole applicationRole;

    private Integer referenceId;

    public UserApplicationRole(Integer userId, ApplicationRole applicationRole) {
        this.userId = userId;
        this.applicationRole = applicationRole;
        this.applicationRoleId = applicationRole.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserApplicationRole that = (UserApplicationRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
