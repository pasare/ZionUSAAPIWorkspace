package org.zionusa.admin.domain.activities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zionusa.admin.domain.GroupMonthlyChallengePoints;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ActivityReportGroup {
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
    private int activitiesCount;
    private int challengesTakenCount;
    private int challengesCompletedCount;
    private String reportGenerationDate;

    private GroupMonthlyChallengePoints groupMonthlyChallengePoints;

}
