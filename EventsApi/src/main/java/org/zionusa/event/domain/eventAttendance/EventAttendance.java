package org.zionusa.event.domain.eventAttendance;

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
@Table(name = "event_attendance")
public class EventAttendance implements BaseDomain<Integer> {
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

    private String title;

    @NotNull(message = "The contact id is required")
    private Integer contactId;

    @NotNull(message = "The event proposal id is required")
    private Integer eventProposalId;
    private Boolean confirmed;
    private Boolean attended;
    private Boolean sameNameFiled;
    private Boolean sameOrganizationAsFiled;
    private Boolean samePositionAsFiled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventAttendance that = (EventAttendance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
