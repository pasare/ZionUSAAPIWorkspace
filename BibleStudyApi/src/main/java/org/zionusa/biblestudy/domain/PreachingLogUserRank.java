package org.zionusa.biblestudy.domain;

import lombok.Data;

@Data
public class PreachingLogUserRank {
    /**
     * @deprecated Use fruitRank instead. Remove this field from May 2021.
     */
    @Deprecated
    private Float fruit;

    private Float fruitGoal = (float) 10;

    private Float fruitPercent = (float) 0;

    private Float fruitRank;

    private Float fruitResult = (float) 0;

    /**
     * @deprecated Use meaningfulRank instead. Remove this field from May 2021.
     */
    @Deprecated
    private Float meaningful;

    private Float meaningfulGoal = (float) 250;

    private Float meaningfulPercent = (float) 0;

    private Float meaningfulRank;

    private Float meaningfulResult = (float) 0;

    /**
     * @deprecated Use simpleRank instead. Remove this field from May 2021.
     */
    @Deprecated
    private Float simple;

    private Float simpleGoal = (float) 1000;

    private Float simplePercent = (float) 0;

    private Float simpleRank;

    private Float simpleResult = (float) 0;

    private Integer userId;
}
