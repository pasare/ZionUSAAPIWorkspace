package org.zionusa.biblestudy.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GoalReport {

    Integer fruitAchieved = 0;

    Long fruitDaysPast;

    Long fruitDaysTotal;

    String fruitEndDate;

    String fruitStartDate;

    Integer fruitGoal = 0;

    Long fruitPace = 0L;

    Long lastGeneratedDateTime;

    Integer members = 0;

    List<MemberGoalReport> reports = new ArrayList<>();

    Integer referenceId;

    String referenceName;

    Long signaturesDaysPast;

    Long signaturesDaysTotal;

    String signaturesEndDate;

    String signaturesStartDate;

    Integer signaturesAchieved = 0;

    Integer signaturesGoal = 0;

    Long signaturesPace = 0L;

}
