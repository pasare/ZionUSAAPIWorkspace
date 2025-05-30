package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.domain.Member;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.biblestudy.dao.ChurchStatusReportDao;
import org.zionusa.biblestudy.dao.SignatureDao;
import org.zionusa.biblestudy.dao.StudentDao;
import org.zionusa.biblestudy.domain.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final ChurchStatusReportDao churchStatusReportDao;
    private final PreachingLogService preachingLogService;
    private final RestService restService;
    private final SignatureDao signatureDao;
    private final StudentDao studentDao;

    @Autowired
    public ReportService(ChurchStatusReportDao churchStatusReportDao,
                         RestService restService,
                         PreachingLogService preachingLogService,
                         SignatureDao signatureDao,
                         StudentDao studentDao) {
        this.churchStatusReportDao = churchStatusReportDao;
        this.restService = restService;
        this.preachingLogService = preachingLogService;
        this.signatureDao = signatureDao;
        this.studentDao = studentDao;
    }

    public List<ChurchStatusReport> findAllChurchStatusReports() {
        return churchStatusReportDao.findAll();
    }

    public List<ChurchStatusReport> findAllChurchStatusReportsByDateBetween(String startDate, String endDate) {
        return churchStatusReportDao.findAllByDateBetween(startDate, endDate);
    }

    public List<ChurchStatusReport> findAllChurchStatusReportsByChurchId(Integer churchId) {
        return churchStatusReportDao.findAllByChurchId(churchId);
    }

    public List<ChurchStatusReport> findAllChurchStatusReportsByChurchIdAndDateBetween(Integer churchId, String startDate, String endDate) {
        return churchStatusReportDao.findAllByChurchIdAndDateBetween(churchId, startDate, endDate);
    }

    public ChurchStatusReport saveChurchStatusReport(Integer churchId, ChurchStatusReport churchStatusReport) {
        return churchStatusReportDao.save(churchStatusReport);
    }

    public void deleteChurchStatusReport(Integer churchId, Integer churchStatusReportId) {
        Optional<ChurchStatusReport> churchStatusReportOptional = churchStatusReportDao.findById(churchStatusReportId);

        if (!churchStatusReportOptional.isPresent())
            throw new NotFoundException("The church status report was not found in the system");

        churchStatusReportDao.delete(churchStatusReportOptional.get());
    }

    @Transactional
    @Cacheable(
            value = "report-fruit-signatures-type-date-range",
            key = "#fruitGoalPerMember.toString()-#fruitStartDate-#fruitEndDate-#signaturesGoalPerMember.toString()" +
                    "-#signaturesStartDate-#signaturesEndDate-#referenceName-#referenceId.toString()"
    )
    public GoalReport getFruitSignaturesReport(
            HttpServletRequest request,
            Integer fruitGoalPerMember,
            String fruitStartDate,
            String fruitEndDate,
            Integer signaturesGoalPerMember,
            String signaturesStartDate,
            String signaturesEndDate,
            String referenceName,
            Integer referenceId) throws Exception {
        logger.warn("Creating a fruit ({} from {} to {}) signature ({} from {} to {}) report per {} id {} ",
                fruitGoalPerMember,
                fruitStartDate,
                fruitEndDate,
                signaturesGoalPerMember,
                signaturesStartDate,
                signaturesEndDate,
                referenceName,
                referenceId);
        // Setup report
        GoalReport goalReport = new GoalReport();

        goalReport.setFruitStartDate(fruitStartDate);
        goalReport.setFruitEndDate(fruitEndDate);
        goalReport.setFruitDaysTotal(daysBetween(fruitStartDate, fruitEndDate));
        goalReport.setFruitDaysPast(ChronoUnit.DAYS.between(LocalDate.parse(fruitStartDate),
                LocalDate.now().atStartOfDay()));

        goalReport.setSignaturesStartDate(signaturesStartDate);
        goalReport.setSignaturesEndDate(signaturesEndDate);
        goalReport.setSignaturesDaysTotal(daysBetween(signaturesStartDate, signaturesEndDate));
        goalReport.setSignaturesDaysPast(ChronoUnit.DAYS.between(LocalDate.parse(signaturesStartDate),
                LocalDate.now().atStartOfDay()));

        goalReport.setReferenceId(referenceId);
        goalReport.setReferenceName(referenceName);

        // Get the related members, baptized students, and general assembly signature reports
        Member[] members = getMembers(request, referenceName, referenceId);
        List<Student> baptizedStudents;
        List<Signature> signatures;

        if (referenceName.equals("user")) {
            baptizedStudents = getBaptizedStudentsByUser(referenceId, fruitStartDate, fruitEndDate);
            signatures = this.signatureDao.getSignaturesByTeacherIdAndGeneralAssemblyUpdatedDateBetween(
                    referenceId, signaturesStartDate, signaturesEndDate);
        } else {
            baptizedStudents = this.studentDao.getStudentsByBaptismDateBetween(fruitStartDate, fruitEndDate);
            signatures = this.signatureDao.findAllByGeneralAssemblyUpdatedDateBetween(
                    signaturesStartDate, signaturesEndDate);
        }

        // Add member based report data
        goalReport.setFruitGoal(fruitGoalPerMember * members.length);
        goalReport.setMembers(members.length);
        goalReport.setSignaturesGoal(signaturesGoalPerMember * members.length);

        // Generate the final user based report
        return generateGoalReport(goalReport, baptizedStudents, members, signatures);
    }

    public List<Student> getBaptizedStudentsByUser(Integer userId, String startDate, String endDate) {
        return this.studentDao.getStudentsByBaptismDateBetween(startDate, endDate)
                .stream()
                .filter(student -> (student.getUserId1() != null && student.getUserId1().equals(userId)) ||
                        (student.getUserId2() != null && student.getUserId2().equals(userId)) ||
                        (student.getUserId3() != null && student.getUserId3().equals(userId)))
                .collect(Collectors.toList());
    }

    public List<PreachingLog.Association> getPreachingTotalsByAssociation(HttpServletRequest request, String startDate, String endDate) {
        return preachingLogService.getTotalsByAssociation(request, startDate, endDate);
    }

    public List<PreachingLog.Church> getPreachingTotalsByChurch(String startDate, String endDate) {
        return preachingLogService.getTotalsByChurch(startDate, endDate);
    }

    /*public List<PreachingLog.Region> getPreachingTotalsByRegion(String startDate, String endDate) {
        return preachingLogService.getTotalsByRegion(startDate, endDate);
    }*/

    public List<PreachingLog.Overseer> getPreachingTotalsByOverseer(HttpServletRequest request, String startDate, String endDate) {
        return preachingLogService.getTotalsByOverseer(request, startDate, endDate);
    }

    public PreachingLogSegments getPreachingTotalsBySegment(HttpServletRequest request, String startDate, String endDate) {
        return preachingLogService.getTotalsBySegment(request, startDate, endDate);
    }

    public List<PreachingLog.PreachingLogTotals> getPreachingTotalsDateBetween(String startDate, String endDate) {
        return preachingLogService.getTotalsDateBetween(startDate, endDate);
    }

    public Integer getTotalCountDateBetween(String startDate, String endDate) {
        List<PreachingLog.PreachingLogTotals> preachingLogTotals = preachingLogService.getTotalsDateBetween(startDate, endDate);
        return preachingLogTotals
                .stream()
                .mapToInt(e -> e.getAcquaintances() + e.getCoWorkers() + e.getFamily() + e.getFriends() + e.getNeighbors() + e.getSimple())
                .reduce(0, Integer::sum);
    }


    private GoalReport generateGoalReport(
            GoalReport goalReport,
            List<Student> baptizedStudents,
            Member[] members,
            List<Signature> signatures
    ) {
        // Create hash maps of the students and the signature reports
        Map<Integer, Integer> baptizedStudentsMap = new HashMap<>();
        Map<Integer, Integer> signatureMap = new HashMap<>();

        for (Student student : baptizedStudents) {
            if (student.getUserId1() != null) {
                baptizedStudentsMap.merge(student.getUserId1(), 1, Integer::sum);
            }
            if (student.getUserId2() != null) {
                baptizedStudentsMap.merge(student.getUserId2(), 1, Integer::sum);
            }
            if (student.getUserId3() != null) {
                baptizedStudentsMap.merge(student.getUserId3(), 1, Integer::sum);
            }
        }

        for (Signature signature : signatures) {
            signatureMap.merge(signature.getTeacherId(), 1, Integer::sum);
        }

        // Apply the result data to each member
        for (Member member : members) {
            Integer memberFruitAchieved = baptizedStudentsMap.get(member.getId()) == null ? 0 :
                    baptizedStudentsMap.get(member.getId());
            goalReport.setFruitAchieved(goalReport.getFruitAchieved() + memberFruitAchieved);

            Integer memberSignaturesAchieved = signatureMap.get(member.getId()) == null ? 0 :
                    signatureMap.get(member.getId());
            goalReport.setSignaturesAchieved(goalReport.getSignaturesAchieved() + memberSignaturesAchieved);

            MemberGoalReport memberGoalReport = new MemberGoalReport();
            memberGoalReport.setFruit(memberFruitAchieved);
            memberGoalReport.setMember(member);
            memberGoalReport.setSignatures(memberSignaturesAchieved);

            goalReport.getReports().add(memberGoalReport);
        }

        // Create the pace letting the user know how much they should have accomplished by now
        if (goalReport.getFruitDaysTotal() != 0) {
            goalReport.setFruitPace(goalReport.getFruitGoal() * goalReport.getFruitDaysPast() / goalReport.getFruitDaysTotal());
        }
        if (goalReport.getSignaturesDaysTotal() != 0) {
            goalReport.setSignaturesPace(goalReport.getSignaturesGoal() * goalReport.getSignaturesDaysPast() / goalReport.getSignaturesDaysTotal());
        }

        // Set the last time the report was generated
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        goalReport.setLastGeneratedDateTime(timestamp.toInstant().toEpochMilli());

        return goalReport;
    }

    @Scheduled(cron = "${cron.report.expire}") // Refresh these reports every 2 hours
    public void evictAllCachesAtIntervals() {
        evictAllCaches();
    }

    @CacheEvict(
            cacheNames = {
                    "report-fruit-signatures-type-date-range"
            },
            allEntries = true
    )
    public void evictAllCaches() {
    }

    private Member[] getMembers(HttpServletRequest request, String referenceName, Integer referenceId) throws Exception {
        if (referenceName.equals("user")) {
            Member member;

            try {
                member = restService.getDisplayMember(request, referenceId);
            } catch (Exception e) {
                throw new NotFoundException();
            }

            return new Member[]{member};
        } else {
            switch (referenceName) {
                case "team":
                    return restService.getAllDisplayTeamMembers(request, referenceId);
                case "group":
                    return restService.getAllDisplayGroupMembers(request, referenceId);
                case "church":
                    return restService.getAllDisplayChurchMembers(request, referenceId);
                default:
                    throw new Exception("Invalid referenceName" + referenceName);
            }
        }
    }

    private Long daysBetween(String startDate, String endDate) {
        LocalDate dateBefore = LocalDate.parse(startDate);
        LocalDate dateAfter = LocalDate.parse(endDate);
        return ChronoUnit.DAYS.between(dateBefore, dateAfter);
    }
}
