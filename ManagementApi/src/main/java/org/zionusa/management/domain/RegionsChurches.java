package org.zionusa.management.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@Table(name = "regions_churches")
public class RegionsChurches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private Integer regionId;

    @OneToOne
    @NotNull(message = "At least one region must be assigned")
    @JoinColumn(name = "regionId", insertable = false, updatable = false)
    private Region region;

    private Integer churchId;

    @OneToOne
    @NotNull(message = "At least one church must be assigned")
    @JoinColumn(name = "churchId", insertable = false, updatable = false)
    private Church church;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RegionsChurches that = (RegionsChurches) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
