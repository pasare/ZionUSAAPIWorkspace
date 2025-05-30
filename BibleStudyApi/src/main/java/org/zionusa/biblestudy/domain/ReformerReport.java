package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReformerReport {

    @EqualsAndHashCode.Include
    private Integer id;

    private String name;

    private String startDate;

    private String endDate;

    private int totalPreached;

    private int totalStudies;

    private int totalFruits;

    private int volunteers;

    private int patriots;

    private int activists;

    private int reformers;

    private int lastReformers;

    private int awardWinners;

    private long lastGeneratedTime;

    List<Reformer> reformerList;

    public ReformerReport() {
        this.totalPreached = 0;
        this.totalStudies = 0;
        this.totalFruits = 0;
        this.volunteers = 0;
        this.patriots = 0;
        this.activists = 0;
        this.reformers = 0;
        this.lastReformers = 0;
        this.reformerList = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        this.setLastGeneratedTime(timestamp.toInstant().toEpochMilli());
    }

    public ReformerReport(Integer id, String name, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
