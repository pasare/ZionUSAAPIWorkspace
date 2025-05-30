package org.zionusa.event.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "university_school_code")
public class UniversitySchoolCode implements BaseDomain<Integer> {
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

    @NotBlank(message = "schoolCode is required")
    private String schoolCode;

    @NotBlank(message = "SchoolName is required")
    private String schoolName;

    private String address;
    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String stateCode;

    private String zipCode;

    private String gaSchoolCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UniversitySchoolCode that = (UniversitySchoolCode) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
