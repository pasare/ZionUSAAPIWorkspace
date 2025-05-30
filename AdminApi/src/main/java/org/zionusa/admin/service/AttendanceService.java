package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.AttendanceDao;
import org.zionusa.admin.domain.Attendance;
import org.zionusa.base.service.BaseService;

import java.util.List;

@Service
public class AttendanceService extends BaseService<Attendance, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceDao attendanceDao;

    @Autowired
    public AttendanceService(AttendanceDao attendanceDao) {
        super(attendanceDao, logger, Attendance.class);
        this.attendanceDao = attendanceDao;
    }

    public List<Attendance> getAttendanceByYear(Integer year) {
        return attendanceDao.getAttendanceByYear(year);
    }

    public List<Attendance> getAttendanceByMonthAndYear(Integer month, Integer year) {
        return attendanceDao.getAttendanceByMonthAndYear(month, year);
    }

    public List<Attendance> getAttendanceByChurch(Integer churchId) {
        return attendanceDao.getAttendanceByChurchId(churchId);
    }

    public List<Attendance> getAttendanceByChurchAndYear(Integer churchId, Integer year) {
        return attendanceDao.getAttendanceByChurchIdAndYear(churchId, year);
    }

    public Attendance getAttendanceByChurchAndMonthAndYear(Integer churchId, Integer month, Integer year) {
        return attendanceDao.getAttendanceByChurchIdAndMonthAndYear(churchId, month, year);
    }

}
