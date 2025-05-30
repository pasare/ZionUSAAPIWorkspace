package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.Attendance;
import org.zionusa.admin.service.AttendanceService;
import org.zionusa.base.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController extends BaseController<Attendance, Integer> {

    private final AttendanceService service;

    @Autowired
    public AttendanceController(AttendanceService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/date/{year}")
    public List<Attendance> getAttendanceByYear(@PathVariable Integer year) {
        return service.getAttendanceByYear(year);
    }

    @GetMapping("/date/{month}/{year}")
    public List<Attendance> getAttendanceByMonthAndYear(@PathVariable Integer month, @PathVariable Integer year) {
        return service.getAttendanceByMonthAndYear(month, year);
    }

    @GetMapping("/church/{churchId}")
    public List<Attendance> getAttendanceByChurch(@PathVariable Integer churchId) {
        return service.getAttendanceByChurch(churchId);
    }

    @GetMapping("/church/{churchId}/{year}")
    public List<Attendance> getAttendanceByChurchAndYear(@PathVariable Integer churchId, @PathVariable Integer year) {
        return service.getAttendanceByChurchAndYear(churchId, year);
    }

    @GetMapping("/church/{churchId}/{month}/{year}")
    public Attendance getAttendanceByChurchAndMonthAndYear(@PathVariable Integer churchId, @PathVariable Integer month, @PathVariable Integer year) {
        return service.getAttendanceByChurchAndMonthAndYear(churchId, month, year);
    }
}
