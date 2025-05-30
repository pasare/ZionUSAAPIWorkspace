package org.zionusa.management.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "TRANSFER_REQUESTS")
public class TransferRequest implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotNull
    Integer userId;
    @Column(name = "member_name")
    String name;
    Integer currentTeamId;
    String currentTeamName;
    Integer currentGroupId;
    String currentGroupName;
    Integer currentChurchId;
    String currentChurchName;
    @NotNull
    Integer newTeamId;
    String newTeamName;
    @NotNull
    Integer newGroupId;
    String newGroupName;
    @NotNull
    Integer newChurchId;
    String newChurchName;
    @NotNull
    String requestStatus;
    Integer reviewerId;
    String reviewerName;
    String comment;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date requestDate;
    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;
    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TransferRequest that = (TransferRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
