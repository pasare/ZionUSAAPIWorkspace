package org.zionusa.biblestudy.domain;

import lombok.Data;

@Data
public class PreachingLogSegments {

    private PreachingLog.PreachingLogTotals all;

    private PreachingLog.PreachingLogTotals association;

    private PreachingLog.PreachingLogTotals mainChurch;

    private PreachingLog.PreachingLogTotals church;

    private PreachingLog.PreachingLogTotals group;

    private PreachingLog.PreachingLogTotals team;

    private PreachingLog.PreachingLogTotals user;

}
