package org.zionusa.admin.dao.activities;

import org.zionusa.admin.domain.activities.ChurchActivityReportView;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface ChurchActivityReportViewDao extends BaseDao<ChurchActivityReportView, String> {

    List<ChurchActivityReportView> findAllByChurchIdAndDateIsBetween(Integer churchId, String startDate, String endDate);

    List<ChurchActivityReportView> findAllByCategoryIdAndChurchTypeIdAndDateIsBetween(
        Integer categoryId,
        Integer churchTypeId,
        String startDate,
        String endDate);

    List<ChurchActivityReportView> findAllByChurchTypeIdAndDateIsBetween(
        Integer churchTypeId,
        String startDate,
        String endDate);

}
