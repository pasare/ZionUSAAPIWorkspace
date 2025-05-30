package org.zionusa.management.domain.branch;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "branches_view")
public class BranchView implements BaseDomain<Integer> {

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

    private String address = "";
    private String city = "";
    private String displayName = "";
    private Boolean distant;
    private String latitude = "";
    private String longitude = "";
    private String name = "";
    private Integer pictureFileId;
    private Integer thumbnailFileId;

    /**
     * Relationships
     */

    private Integer associationId;
    private boolean associationArchived;
    private boolean associationHidden;
    private Integer associationLeaderId;
    private Integer associationLeaderTwoId;
    private String associationName;
    private String associationPictureFileId;
    private String associationThumbnailFileId;

    private Integer leaderId;
    private String leaderDisplayName;
    private Integer leaderTwoId;
    private String leaderTwoDisplayName;

    private Integer mainBranchId;
    private boolean mainBranchArchived;
    private boolean mainBranchHidden;
    private Integer mainBranchLeaderId;
    private Integer mainBranchLeaderTwoId;
    private String mainBranchName;
    private String mainBranchPictureFileId;
    private String mainBranchThumbnailFileId;

    private Integer parentBranchId;

    private Integer stateId;
    private String stateFullName;
    private String stateShortName;

    private Integer typeId;
    private String typeName;
}
