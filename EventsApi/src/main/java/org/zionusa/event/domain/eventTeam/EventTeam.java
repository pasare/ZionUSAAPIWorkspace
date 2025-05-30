package org.zionusa.event.domain.eventTeam;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@AllArgsConstructor
@Table(name = "event_teams", uniqueConstraints = {@UniqueConstraint(columnNames = {"branchId"})})
public class EventTeam implements BaseDomain<Integer> {

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

    @NotNull(message = "The branchId is required")
    private Integer branchId;

    private String adminIds;

    private String managerIds;

    private String gaIds;

    private String eventRepresentativeIds;

    private String photographerIds;

    private String videographerIds;

    private String graphicDesignerIds;

    private String audioVisualEngineerIds;

    private String publicRelationsRepresentativeIds;

    @NotNull(message = "deletable status is required")
    private boolean deletable = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventTeam eventTeam = (EventTeam) o;
        return Objects.equals(id, eventTeam.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
