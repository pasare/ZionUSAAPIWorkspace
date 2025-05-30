package org.zionusa.admin.domain.activities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zionusa.admin.domain.GoalLog;
import org.zionusa.admin.domain.UserMonthlyChallengePoints;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ActivityReportMember {
    @Id
    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    private String displayName;
    private String profilePictureUrl;

    private int activitiesCount;
    private int challengesTakenCount;
    private int challengesCompletedCount;

    private UserMonthlyChallengePoints userMonthlyChallengePoints;

    private List<GoalLog> challenges;

    private String reportGenerationDate;
}
