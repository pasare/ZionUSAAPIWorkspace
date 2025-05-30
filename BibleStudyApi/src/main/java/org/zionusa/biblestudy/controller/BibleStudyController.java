package org.zionusa.biblestudy.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.BibleStudy;
import org.zionusa.biblestudy.service.BibleStudyService;

import java.util.List;

@RestController
@RequestMapping("/bible-studies")
public class BibleStudyController extends BaseController<BibleStudy, Integer> {

    private final BibleStudyService bibleStudyService;

    @Autowired
    public BibleStudyController(BibleStudyService bibleStudyService) {
        super(bibleStudyService);
        this.bibleStudyService = bibleStudyService;
    }

    @GetMapping(value = "/{startDate}/{endDate}")
    List<BibleStudy> getByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return bibleStudyService.getByDate(startDate, endDate);
    }

    @GetMapping(value = "/parent-church/{parentChurchId}")
    List<BibleStudy> getByParentChurchId(@PathVariable Integer parentChurchId) {
        return bibleStudyService.getByParentChurch(parentChurchId);
    }

    @GetMapping(value = "/church/{churchId}")
    List<BibleStudy> getByChurch(@PathVariable Integer churchId) {
        return bibleStudyService.getByChurch(churchId);
    }

    @GetMapping(value = "/church/{churchId}/{startDate}/{endDate}")
    List<BibleStudy> getByChurchAndDate(@PathVariable Integer churchId,
                                        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return bibleStudyService.getByChurchAndDate(churchId, startDate, endDate);
    }

    @GetMapping(value = "/student/{studentId}")
    List<BibleStudy> getByStudent(@PathVariable Integer studentId) {
        return bibleStudyService.getApprovedAttendedByStudent(studentId);
    }

    @GetMapping(value = "/student/{studentId}/history")
    List<BibleStudy> getAllByStudent(@PathVariable Integer studentId) {
        return bibleStudyService.getAllByStudent(studentId);
    }

    @GetMapping(value = "/student/{studentId}/{startDate}/{endDate}")
    List<BibleStudy> findByStudentIdAndArchivedAndDateBetweenAndDeniedIsFalseAndApprovedIsTrueAndAttendedIsTrue(@PathVariable Integer studentId,
                                                                                                                @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                                                                                                @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        return bibleStudyService.getByStudentAndDate(studentId, startDate, endDate);
    }

    @PutMapping(value = "/{id}/teacher-available")
    public BibleStudy updateTeacherStatusAvailable(@PathVariable Integer id) throws NotFoundException {
        return bibleStudyService.teacherAvailable(id);
    }

    @PutMapping(value = "/{id}/teacher-unavailable")
    public BibleStudy updateTeacherStatusUnavailable(@PathVariable Integer id) throws NotFoundException {
        return bibleStudyService.teacherUnavailable(id);
    }

    @PutMapping(value = "/{id}/approve")
    public BibleStudy approveBibleStudy(@PathVariable Integer id) throws NotFoundException {
        return bibleStudyService.approve(id);
    }

    @PutMapping(value = "/{id}/deny")
    public BibleStudy denyBibleStudy(@PathVariable Integer id) throws NotFoundException {
        return bibleStudyService.deny(id);
    }

    @PutMapping(value = "/{id}/attend")
    public BibleStudy attendBibleStudy(@PathVariable Integer id) throws NotFoundException {
        return bibleStudyService.attend(id);
    }

    @PutMapping(value = "/{id}/archive")
    public void archiveBibleStudy(@PathVariable Integer id) {
        bibleStudyService.archive(id);
    }
}
