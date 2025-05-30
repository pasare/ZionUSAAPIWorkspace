package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.biblestudy.dao.*;
import org.zionusa.biblestudy.domain.BibleStudy;
import org.zionusa.biblestudy.domain.Student;
import org.zionusa.biblestudy.domain.StudentStep;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService extends BaseService<Student, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final BibleStudyDao bibleStudyDao;
    private final StudentDao studentDao;
    private final StudentStepDao studentStepDao;
    private final StudentLostSheepDao studentLostSheepDao;
    private final LostSheepReportDao lostSheepReportDao;

    @Autowired
    public StudentService(BibleStudyDao bibleStudyDao,
                          StudentDao studentDao,
                          StudentStepDao studentStepDao,
                          StudentLostSheepDao studentLostSheepDao,
                          LostSheepReportDao lostSheepReportDao) {
        super(studentDao, logger, Student.class);
        this.bibleStudyDao = bibleStudyDao;
        this.studentDao = studentDao;
        this.studentStepDao = studentStepDao;
        this.studentLostSheepDao = studentLostSheepDao;
        this.lostSheepReportDao = lostSheepReportDao;
    }

    @Override
    public void delete(Integer studentId) throws NotFoundException {
        Student student = getById(studentId);

        List<BibleStudy> bibleStudies = bibleStudyDao.findByStudentIdAndArchivedAndDeniedIsFalse(student.getId(), false);
        if (bibleStudies != null) bibleStudyDao.deleteAll(bibleStudies);

        studentDao.delete(student);
        evictAllCache();
    }

    public void archive(Integer id) {
        Student student = getById(id);

        if (student == null) {
            throw new NotFoundException("Cannot archive a student that does not exist");
        }

        student.setArchived(true);
        studentDao.save(student);
        evictAllCache();
    }


    @PreAuthorize("hasAuthority('Admin')")
    public void forceCacheEvict() {
        evictAllCache();
    }

    public List<Student> getByParentChurch(Integer parentChurchId) {
        return studentDao.findByChurchIdAndArchivedIsFalse(parentChurchId);
    }

    public List<Student.LostSheep> getStudentByLostSheepStatusAndChurch(Integer churchId) {
        return studentLostSheepDao.findByChurchId(churchId);
    }

    public List<Student> getByChurch(Integer churchId, Boolean lostSheep) {
        if (lostSheep == null) {
            return studentDao.findByChurchIdAndArchivedIsFalse(churchId);
        }
        return studentDao.findByChurchIdAndArchivedIsFalseAndLostSheep(churchId, lostSheep);
    }

    @Cacheable(value = "student-church-user-cache-student", key = "#churchId.toString()-#userId.toString()")
    public List<Student> getByChurchAndUser(Integer churchId, Integer userId) {
        List<Student> students = studentDao.findByChurchIdOrUserId1OrUserId2OrUserId3AndArchivedIsFalse(churchId, userId, userId,
            userId);
        return students.stream().filter(student -> !student.isArchived()).collect(Collectors.toList());
    }


    public List<Student> getByUser(Integer userId) {
        List<Student> students = studentDao.findAllByUserId1OrUserId2OrUserId3(userId, userId, userId);
        return students.stream().filter(student -> !student.isArchived()).collect(Collectors.toList());
    }

    public Student baptizeStudent(Integer id, Student.BaptismDetails baptismDetails) throws NotFoundException {
        Student student = getById(id);

        student.setBaptismDate(baptismDetails.getBaptismDate());
        student.setBaptismStudyId(baptismDetails.getBaptismStudyId());
        return studentDao.save(student);
    }

    @Cacheable(value = "student-step-cache-student", key = "#studentId.toString()")
    public StudentStep getStudentStepByStudentId(Integer studentId) throws NotFoundException {
        Optional<StudentStep> optionalStudentStep = studentStepDao.findById(studentId);

        if (optionalStudentStep.isPresent()) {
            return optionalStudentStep.get();
        }

        throw new NotFoundException();
    }

    @Cacheable(value = "student-step-cache-user", key = "#userId.toString()")
    public List<StudentStep> getStudentStepByUserId(Integer userId) {
        return studentStepDao.getAllByDevelopRelationshipNotNullAndUserId1OrUserId2OrUserId3(userId, userId, userId);
    }

    @Cacheable(value = "student-step-cache-user-date-between", key = "#userId.toString()-#startDate-#endDate")
    public List<StudentStep> getStudentStepByUserIdDateBetween(Integer userId, String startDate, String endDate) throws ResponseStatusException {
        return studentStepDao.getAllByDevelopRelationshipBetweenAndUserId1OrUserId2OrUserId3(startDate, endDate, userId, userId,
            userId);
    }

    public List<Student> getStudentByLostSheepStatusDateBetween(Boolean lostSheep, String date) throws ResponseStatusException {
        return studentDao.getAllByLostSheepAndBaptismDateLessThanEqual(lostSheep, date);
    }

    public List<Student> getAllByLostSheepAndBaptismDateLessThanEqualAndUserId1OrUserId2OrUserId3(Boolean lostSheep, String date, int userId) throws ResponseStatusException {
        return studentDao.getAllByUserId1OrUserId2OrUserId3AndLostSheepAndBaptismDateLessThanEqual(lostSheep, userId, userId, userId, date);
    }

    public List<Student.LostSheepReport> getAllChurchLostSheepReport() {
        return lostSheepReportDao.findAll();
    }

    public Student.LostSheepReport getLostSheepReportByChurchId(int churchId) {
        Optional<Student.LostSheepReport> lostSheepReport = lostSheepReportDao.findById(churchId);
        return lostSheepReport.orElse(null);
    }

    @Override
    public Student save(Student student) {
        Student updatedStudent = super.save(student);
        evictAllCache();
        return updatedStudent;
    }

    @CacheEvict(
        cacheNames = {
            "student-church-user-cache-student",
            "student-step-cache-student",
            "student-step-cache-user",
            "student-step-cache-user-date-between",
        },
        allEntries = true
    )
    public void evictAllCache() {
    }


}
