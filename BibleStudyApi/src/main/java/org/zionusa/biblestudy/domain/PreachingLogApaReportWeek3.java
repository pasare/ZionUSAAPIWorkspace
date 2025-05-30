package org.zionusa.biblestudy.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@Table(name = "preaching_log_apa_week_3_view")
public class PreachingLogApaReportWeek3 implements IPreachingLogApaReport {
    @Id
    private Integer userId;
    private Integer activityPoints;
    private Integer churchId;
    private String churchName;
    private String displayName;
    private Integer preachingPoints;
    private Integer numLogs;
    private String pictureThumbnailUrl;
    private Integer teachingPoints;
    private Integer totalPoints;
}
