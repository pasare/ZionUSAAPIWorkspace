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
@Table(name = "event_types")
public class EventType implements BaseDomain<Integer> {
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

    private Integer eventCategoryId;

    @NotEmpty(message = "The title is required")
    private String title;

    private String category;

    private String email;

    @Column(columnDefinition = "bit not null default 1")
    private Boolean active;

    @NotNull(message = "deletable status is required")
    private boolean deletable = true;

    @Column(columnDefinition = "bit not null default 0")
    private boolean defaultInternalEvent;

    @Column(columnDefinition = "bit not null default 0")
    private boolean defaultPrivateEvent;

    @Column(columnDefinition = "text not null default '[]'")
    private String requiredTitles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventType eventType = (EventType) o;
        return Objects.equals(id, eventType.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
