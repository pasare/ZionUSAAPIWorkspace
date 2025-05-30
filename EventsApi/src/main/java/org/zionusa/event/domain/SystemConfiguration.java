package org.zionusa.event.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@AllArgsConstructor
@Table(name = "system_configurations")
public class SystemConfiguration implements BaseDomain<Integer> {

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

    @NotEmpty(message = "The category is required")
    private String category;
    private String configurationKey;
    private String configurationValue;

    @NotNull(message = "deletable status is required")
    private boolean deletable = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SystemConfiguration that = (SystemConfiguration) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
