package org.zionusa.event.domain.eventGroup;

import lombok.*;
import org.hibernate.Hibernate;
import org.zionusa.event.enums.EventGroupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "event_groups", uniqueConstraints = {@UniqueConstraint(columnNames = {"eventCategoryId", "type", "value"})})
public class EventGroup {
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
    private String displayName;

    @NotNull
    private String value;

    @NotNull
    private EventGroupType type;

    // Null here means it is global
    private Integer eventCategoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventGroup that = (EventGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
