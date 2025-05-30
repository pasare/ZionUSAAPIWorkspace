package org.zionusa.management.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.util.constraints.DateFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_password_reset", uniqueConstraints = {@UniqueConstraint(columnNames = {"churchId", "email"})})
public class UserPasswordReset {
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "The church id is required")
    private Integer churchId;

    @NotNull(message = "The church name is required")
    private String churchName;

    @NotEmpty(message = "The first name is required")
    private String firstName;

    @NotEmpty(message = "The last name is required")
    private String lastName;

    @NotEmpty(message = "The username is required")
    private String userName;

    @NotEmpty(message = "The email address is required")
    private String email;

    @DateFormatConstraint
    private String determinedDate;

    private Boolean approved;

    private Integer approverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPasswordReset that = (UserPasswordReset) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
