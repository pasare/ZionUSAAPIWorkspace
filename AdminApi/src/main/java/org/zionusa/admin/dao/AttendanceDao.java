package org.zionusa.admin.dao;

import org.zionusa.admin.domain.Attendance;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface AttendanceDao extends BaseDao<Attendance, Integer> {
    List<Attendance> getAttendanceByChurchId(Integer churchId);

    List<Attendance> getAttendanceByMonthAndYear(Integer month, Integer year);

    List<Attendance> getAttendanceByYear(Integer year);

    List<Attendance> getAttendanceByChurchIdAndYear(Integer churchId, Integer year);

    Attendance getAttendanceByChurchIdAndMonthAndYear(Integer churchId, Integer month, Integer year);
}
