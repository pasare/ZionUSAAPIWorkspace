package org.zionusa.management.domain.mainbranch;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "main_branches_view")
public class MainBranchView implements BaseDomain<Integer> {

    /**
     * Base Properties
     */

    @Id
    @Column(name = "id")
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    /**
     * Properties
     */

    private String name = "";

    /**
     * Relationships
     */

    private Integer associationId;
    private boolean associationArchived;
    private boolean associationHidden;
    private Integer associationLeaderId;
    private Integer associationLeaderTwoId;
    private String associationName;
    private String associationPictureUrl;

    private Integer leaderId;
    // TODO: private String leaderBranchName;
    private String leaderDisplayName;

    private Integer leaderTwoId;
    // TODO: private String leaderTwoBranchName;
    private String leaderTwoDisplayName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MainBranchView that = (MainBranchView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
