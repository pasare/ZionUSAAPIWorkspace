package org.zionusa.admin.dao.activities;

import org.zionusa.admin.domain.activities.GroupActivityReportView;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface GroupActivityReportViewDao extends BaseDao<GroupActivityReportView, String> {

    List<GroupActivityReportView> findAllByGroupIdAndDateIsBetween(Integer groupId, String startDate, String endDate);

    List<GroupActivityReportView> findAllByChurchIdAndDateIsBetween(
        Integer churchId,
        String startDate,
        String endDate
    );
}
