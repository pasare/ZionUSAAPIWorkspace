package org.zionusa.event.domain.eventPresenter;

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
@Table(name = "event_presenter")
public class EventPresenter implements BaseDomain<Integer> {
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
    private Integer branchId;
    @NotNull
    private String branchName;
    @NotNull
    private String displayName;
    @NotNull
    private String username;
    @NotNull
    private Integer userId;

    private Integer eventProposalId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventPresenter presenter = (EventPresenter) o;
        return Objects.equals(id, presenter.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
