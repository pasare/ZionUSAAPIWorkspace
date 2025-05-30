package org.zionusa.admin.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.domain.Member;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MovementUserLevelReport implements BaseDomain<String> {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    private Integer churchId;

    private Integer groupId;

    private Integer movementId;

    private Integer level;

    private String profilePictureUrl;

    private Integer teamId;

    private Integer totalCount;

    private Integer userId;

    private String userName;

    public MovementUserLevelReport(MovementUserLevel movementUserLevel, Member member) {
        this.id = movementUserLevel.getId();
        this.movementId = movementUserLevel.getMovementId();
        this.level = movementUserLevel.getLevel();
        this.totalCount = movementUserLevel.getTotalCount();
        this.userId = movementUserLevel.getUserId();
        this.churchId = member.getChurchId();
        this.groupId = member.getGroupId();
        this.profilePictureUrl = member.getPictureUrl();
        this.teamId = member.getTeamId();
        this.userName = member.getDisplayName();
    }
}
