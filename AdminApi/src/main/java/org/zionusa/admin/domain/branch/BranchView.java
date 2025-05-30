package org.zionusa.admin.domain.branch;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "branch_view")
public class BranchView implements BaseDomain<Integer> {

    /**
     * Base Properties
     */

    @Id
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    /**
     * Properties
     */
    private Integer associationId;
    private String associationName;
    private String displayName;
    private Integer leaderId;
    private Integer leaderTwoId;
    private Integer mainBranchId;
    private String name;
    private Integer stateId;
}
