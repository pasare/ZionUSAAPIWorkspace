package org.zionusa.biblestudy.controller;

import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.Student;
import org.zionusa.biblestudy.domain.StudentStep;
import org.zionusa.biblestudy.service.AzureBlobStorageService;
import org.zionusa.biblestudy.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController extends BaseController<Student, Integer> {

    private final StudentService studentService;
    private final AzureBlobStorageService azureBlobStorageService;

    @Autowired
    public StudentController(StudentService studentService, AzureBlobStorageService azureBlobStorageService) {
        super(studentService);
        this.studentService = studentService;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @PutMapping(value = "/{id}/archive")
    public void archiveStudent(@PathVariable Integer id) {
        this.studentService.archive(id);
    }


    @GetMapping(value = "/cache/evict")
    public void forceCacheEvict() {
        this.studentService.forceCacheEvict();
    }

    @GetMapping(value = "/church/{churchId}")
    public List<Student> getStudentsByChurch(@PathVariable Integer churchId, @RequestParam(value = "lostSheep", required = false) Boolean lostSheep) {
        return this.studentService.getByChurch(churchId, lostSheep);
    }

    @GetMapping(value = "/church/{churchId}/user/{userId}")
    public List<Student> getStudentsByChurchAndUser(@PathVariable Integer churchId, @PathVariable Integer userId) {
        return this.studentService.getByChurchAndUser(churchId, userId);
    }

    @GetMapping(value = "/user/{userId}")
    public List<Student> getStudentsByUser(@PathVariable Integer userId) {
        return this.studentService.getByUser(userId);
    }

    @GetMapping(value = "/parent-church/{churchId}")
    public List<Student> getStudentsByParentChurch(@PathVariable Integer churchId) {
        return this.studentService.getByParentChurch(churchId);
    }

    @PutMapping(value = "/{id}/baptize")
    public Student baptizeStudent(@PathVariable Integer id, @RequestBody Student.BaptismDetails baptismStudent) throws NotFoundException {
        return this.studentService.baptizeStudent(id, baptismStudent);
    }

    @GetMapping(value = "/step/{studentId}")
    public StudentStep getStudentStepByStudentId(@PathVariable Integer studentId) {
        return this.studentService.getStudentStepByStudentId(studentId);
    }

    @GetMapping(value = "/step/user/{userId}")
    @ApiOperation(value = "Get students step in the circle of life by branch user")
    public List<StudentStep> getStudentStepByUserId(
        @PathVariable Integer userId) {
        return this.studentService.getStudentStepByUserId(userId);
    }

    @GetMapping(value = "/step/user/{userId}/{startDate}/{endDate}")
    @ApiOperation(value = "Get students step in the circle of life by branch user within a date range")
    public List<StudentStep> getStudentStepByUserIdDateBetween(
        @PathVariable Integer userId,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return this.studentService.getStudentStepByUserIdDateBetween(userId, startDate, endDate);
    }

    @GetMapping(value = "/lost-sheep/{lostSheep}/{date}")
    @ApiOperation(value = "Get students by lost sheep status and date range")
    public List<Student> getStudentStepByLostSheepStatusDateBetween(
        @PathVariable Boolean lostSheep,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String date) {
        return this.studentService.getStudentByLostSheepStatusDateBetween(lostSheep, date);
    }

    @GetMapping(value = "/lost-sheep/{lostSheep}/{date}/{userId}")
    @ApiOperation(value = "Get students by lost sheep status and date range and userId")
    public List<Student> getAllByLostSheepAndBaptismDateLessThanEqualAndUserId1OrUserId2OrUserId3(
        @PathVariable Boolean lostSheep,
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
        @PathVariable int userId) {
        return this.studentService.getAllByLostSheepAndBaptismDateLessThanEqualAndUserId1OrUserId2OrUserId3(lostSheep, date, userId);
    }

    @GetMapping(value = "/lost-sheep/church/{churchId}")
    @ApiOperation(value = "Get students by lost sheep status and church from Nov 1, 2020 to Dec 31, 2020")
    public List<Student.LostSheep> getStudentByLostSheepStatusAndChurch(@PathVariable Integer churchId) {
        return this.studentService.getStudentByLostSheepStatusAndChurch(churchId);
    }

    @GetMapping(value = "/lost-sheep/reports/churches")
    @ApiOperation(value = "Get students by lost sheep status and date range and userId")
    public List<Student.LostSheepReport> getAllChurchLostSheepReports() {
        return this.studentService.getAllChurchLostSheepReport();
    }

    @GetMapping(value = "/lost-sheep/reports/churches/{churchId}")
    @ApiOperation(value = "Get students by lost sheep status and date range and userId")
    public Student.LostSheepReport getLostSheepReportsByChurchId(@PathVariable int churchId) {
        return this.studentService.getLostSheepReportByChurchId(churchId);
    }

    @PostMapping(value = "/upload")
    public String upload(@RequestParam("userId") Integer userId,
                         @RequestParam("name") String name,
                         @RequestParam(value = "file") MultipartFile file) {

        return azureBlobStorageService.uploadImage(userId, file, name, "students");
    }
}
