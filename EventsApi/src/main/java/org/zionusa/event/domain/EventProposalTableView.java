package org.zionusa.event.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "event_proposals_table_view")
public class EventProposalTableView implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;
    private Integer branchId;
    private String branchName;
    private Integer categoryId;
    private String categoryName;
    private Integer locationId;
    private String locationName;
    private String proposedDate;
    private String title;
    private Integer typeId;
    private String typeName;
    private String workflowStatus;

    private Integer contactsTotal;
    private Integer membersTotal;
    private Integer participantsTotal;
    private Integer vipsTotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventProposalTableView that = (EventProposalTableView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
