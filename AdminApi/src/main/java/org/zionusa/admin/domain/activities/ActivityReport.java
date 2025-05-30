package org.zionusa.admin.domain.activities;

import lombok.Data;

import javax.persistence.Id;

@Data
public class ActivityReport {
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
