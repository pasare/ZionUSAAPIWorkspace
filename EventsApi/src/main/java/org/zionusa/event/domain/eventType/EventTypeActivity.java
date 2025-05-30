package org.zionusa.event.domain.eventType;

import lombok.*;
import org.hibernate.Hibernate;
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
@AllArgsConstructor
@Table(name = "event_types_activity")
public class EventTypeActivity implements BaseDomain<Integer> {
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

    private Integer eventTypeId;

    @NotEmpty(message = "The title is required")
    private String title;

    @Column(columnDefinition = "bit not null default 1")
    private Boolean active;

    @NotNull(message = "deletable status is required")
    private boolean deletable = true;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventTypeActivity eventTypeCategory = (EventTypeActivity) o;
        return Objects.equals(id, eventTypeCategory.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
