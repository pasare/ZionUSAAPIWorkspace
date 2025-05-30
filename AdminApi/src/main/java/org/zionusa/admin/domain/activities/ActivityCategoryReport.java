package org.zionusa.admin.domain.activities;

import lombok.Data;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
public class ActivityCategoryReport {
    List<ActivityReport> activities = new ArrayList<>();
    @Id
    private Integer id;
    private String abbreviation;
    private Double avgParticipationCount = 0.0;
    private String backgroundColor;
    private Integer count = 0;
    private String iconUrl;
    private String name;
    private Integer participantCount = 0;
    private String percentage = "0.0%";
    private String textColor;
}
