package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PreachingLogReport {
    public static String ALL_LEVEL = "All";
    public static String ASSOCIATION_LEVEL = "Association";
    public static String MAIN_CHURCH_LEVEL = "Main Church";
    public static String CHURCH_LEVEL = "Church";
    public static String GROUP_LEVEL = "Group";
    public static String TEAM_LEVEL = "Team";

    @EqualsAndHashCode.Include
    private Integer id;

    private String level;

    private PreachingLog.PreachingLogTotals report;

    private List<PreachingLogReport> children;

    public PreachingLogReport() {
        this.children = new ArrayList<>();
    }

    public PreachingLogReport(String level) {
        this.level = level;
        this.children = new ArrayList<>();
    }
}
