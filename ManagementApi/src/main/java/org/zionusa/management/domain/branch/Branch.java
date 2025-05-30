package org.zionusa.management.domain.branch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.enums.EBranchType;
import org.zionusa.management.domain.User;
import org.zionusa.management.domain.branchtype.BranchType;
import org.zionusa.management.domain.file.File;
import org.zionusa.management.domain.mainbranch.MainBranch;
import org.zionusa.management.domain.state.State;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "branches")
public class Branch implements BaseDomain<Integer> {

    /**
     * Base Properties
     */

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

    /**
     * Properties
     */

    private String address = "";

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean distant = false;

    private String city = "";

    private String latitude = "";

    private String longitude = "";

    @NotNull
    private String name = "";

    /**
     * Relationships
     */

    private Integer leaderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderId", insertable = false, updatable = false)
    @ToString.Exclude
    private User leader;

    private Integer leaderTwoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderTwoId", insertable = false, updatable = false)
    @ToString.Exclude
    private User leaderTwo;

    @NotNull
    private Integer mainBranchId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mainBranchId", insertable = false, updatable = false)
    @ToString.Exclude
    private MainBranch mainBranch;

    private Integer parentBranchId;

    private String pictureFileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pictureFileId", insertable = false, updatable = false)
    @ToString.Exclude
    private File picture;

    @NotNull
    private Integer stateId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stateId", insertable = false, updatable = false)
    @ToString.Exclude
    private State state;

    private String thumbnailFileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnailFileId", insertable = false, updatable = false)
    @ToString.Exclude
    private File thumbnail;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EBranchType typeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId", insertable = false, updatable = false)
    @ToString.Exclude
    private BranchType type;

}
