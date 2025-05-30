package org.zionusa.event.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "event_status_groups", uniqueConstraints = {@UniqueConstraint(columnNames = {"eventGroupId"})})
public class EventStatusGroup {

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

    @NotNull
    private Integer eventGroupId;

    private Boolean started;
    private Boolean finalized;
    private Boolean managerApproved;
    private Boolean adminApproved;
    private Boolean gaSubmitted;
    private Boolean gaApproved;
    private Boolean needsRevision;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventStatusGroup that = (EventStatusGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
