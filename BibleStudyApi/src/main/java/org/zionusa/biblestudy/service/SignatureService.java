package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.domain.Member;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.biblestudy.dao.SignatureDao;
import org.zionusa.biblestudy.dao.SignatureGeneralAssemblyMovementDao;
import org.zionusa.biblestudy.dao.SignatureGraderDao;
import org.zionusa.biblestudy.dao.StudyDao;
import org.zionusa.biblestudy.domain.Signature;
import org.zionusa.biblestudy.domain.Study;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SignatureService extends BaseService<Signature, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SignatureService.class);

    private final RestService restService;
    private final SignatureDao signatureDao;
    private final SignatureGeneralAssemblyMovementDao signatureGeneralAssemblyMovementDao;
    private final StudyDao studyDao;
    private final SignatureGraderDao signatureGraderDao;

    @Autowired
    public SignatureService(RestService restService,
                            SignatureDao signatureDao,
                            SignatureGeneralAssemblyMovementDao signatureGeneralAssemblyMovementDao,
                            StudyDao studyDao, SignatureGraderDao signatureGraderDao) {
        super(signatureDao, logger, Signature.class);
        this.restService = restService;
        this.signatureDao = signatureDao;
        this.signatureGeneralAssemblyMovementDao = signatureGeneralAssemblyMovementDao;
        this.studyDao = studyDao;
        this.signatureGraderDao = signatureGraderDao;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        super.delete(id);
        evictAllCache();
    }

    public void evaluateSignature(Signature.SignatureEvaluation signatureEvaluation) throws Exception {
        Signature signature = this.signatureDao.findSignatureByTeacherIdAndStudyId(
            signatureEvaluation.teacherId,
            signatureEvaluation.studyId

        );

        if (signature == null) {
            signature = new Signature();
            signature.setGeneralAssembly(0);
            signature.setReady(0);
            signature.setTeaching(0);
            signature.setStudyId(signatureEvaluation.studyId);
            signature.setTeacherId(signatureEvaluation.teacherId);
            signature.setTeacherChurchName(signatureEvaluation.teacherChurchName);
        }
        signature.setTeacherName(signatureEvaluation.getTeacherName());

        switch (signatureEvaluation.gradeType) {
            case "GA":
                signature.setGeneralAssembly(signature.getGeneralAssembly() + 1);
                signature.setGeneralAssemblyUpdatedDate(signatureEvaluation.getGraderDate());
                signature.setGeneralAssemblyGraderId(signatureEvaluation.getGraderId());
                signature.setGeneralAssemblyGraderName(signatureEvaluation.getGraderName());
                break;
            case "R":
                signature.setReady(signature.getReady() + 1);
                signature.setReadyUpdatedDate(signatureEvaluation.getGraderDate());
                signature.setReadyGraderId(signatureEvaluation.getGraderId());
                signature.setReadyGraderName(signatureEvaluation.getGraderName());
                break;
            case "T":
                signature.setTeaching(signature.getTeaching() + 1);
                signature.setTeachingUpdatedDate(signatureEvaluation.getGraderDate());
                signature.setTeachingGraderId(signatureEvaluation.getGraderId());
                signature.setTeachingGraderName(signatureEvaluation.getGraderName());
                break;
            default:
                throw new Exception("There is no evaluation gradeType " + signatureEvaluation.gradeType);
        }

        this.save(signature);

        // TODO: Used materialized views so we don't have to cache this
        evictAllCache();

        // TODO: Send notification to the teacher
    }

    @PreAuthorize("hasAuthority('Admin')")
    public void forceCacheEvict() {
        evictAllCache();
    }

    public Collection<Signature.SignatureCount> getSignatureCount() {
        Map<Integer, Signature.SignatureCount> signatureCountMap = new HashMap<>();
        List<Signature> signatures = signatureDao.findAll();

        for (Signature signature : signatures) {
            Signature.SignatureCount signatureCount = signatureCountMap.get(signature.getTeacherId());
            if (signatureCount == null)
                signatureCount = new Signature.SignatureCount(signature.getTeacherId(), 0, 0, 0);

            signatureCount.setGeneralAssembly(signatureCount.getGeneralAssembly());

            signatureCount.setReady(signatureCount.getReady());

            signatureCount.setTeaching(signatureCount.getTeaching());

            signatureCountMap.put(signature.getTeacherId(), signatureCount);
        }
        return signatureCountMap.values();
    }

    public List<Signature.SignatureGeneralAssemblyMovement> getGeneralAssemblyMovementSignatureReports() {
        return this.signatureGeneralAssemblyMovementDao.findAll();
    }

    public List<Signature.SignatureGeneralAssemblyMovementReport> getGeneralAssemblySignatureMovementTop300Reports(HttpServletRequest request, Integer top) {
        PageRequest pageRequest = top == null ? null : PageRequest.of(0, top, Sort.by("totalCompleted").descending());
        List<Signature.SignatureGeneralAssemblyMovement> signatureGeneralAssemblyMovements = (pageRequest == null)
            ? this.signatureGeneralAssemblyMovementDao.findAll()
            : this.signatureGeneralAssemblyMovementDao.findAllBy(pageRequest);

        Member[] members = restService.getAllDisplayMembers(request);

        return getGeneralAssemblyMovementSignatureReport(members, signatureGeneralAssemblyMovements);
    }

    public List<Signature.SignatureGeneralAssemblyMovementReport> getGeneralAssemblySignatureMovementReportByChurchId(HttpServletRequest request, Integer churchId) {
        List<Signature.SignatureGeneralAssemblyMovement> signatureGeneralAssemblyMovements = getGeneralAssemblyMovementSignatureReports();

        Member[] members = restService.getAllDisplayChurchMembers(request, churchId);

        return getGeneralAssemblyMovementSignatureReport(members, signatureGeneralAssemblyMovements);
    }

    public List<Signature.SignatureGeneralAssemblyMovementReport> getGeneralAssemblySignatureMovementReportByGroupId(HttpServletRequest request, Integer groupId) {
        List<Signature.SignatureGeneralAssemblyMovement> signatureGeneralAssemblyMovements = getGeneralAssemblyMovementSignatureReports();

        Member[] members = restService.getAllDisplayGroupMembers(request, groupId);

        return getGeneralAssemblyMovementSignatureReport(members, signatureGeneralAssemblyMovements);
    }

    public List<Signature.SignatureGeneralAssemblyMovementReport> getGeneralAssemblySignatureMovementReportByTeamId(HttpServletRequest request, Integer teamId) {
        List<Signature.SignatureGeneralAssemblyMovement> signatureGeneralAssemblyMovements = getGeneralAssemblyMovementSignatureReports();

        Member[] members = restService.getAllDisplayTeamMembers(request, teamId);

        return getGeneralAssemblyMovementSignatureReport(members, signatureGeneralAssemblyMovements);
    }

    public Signature.SignatureGeneralAssemblyMovementReport getGeneralAssemblyMovementSignatureReportByTeacherId(HttpServletRequest request, Integer teacherId) throws NotFoundException {
        Signature.SignatureGeneralAssemblyMovement signatureGeneralAssemblyMovement = this.signatureGeneralAssemblyMovementDao.getByTeacherId(teacherId);

        Member member = restService.getDisplayMember(request, teacherId);

        if (member == null) {
            throw new NotFoundException();
        }

        return new Signature.SignatureGeneralAssemblyMovementReport(signatureGeneralAssemblyMovement, member);
    }

    public List<Signature> getByTeacherId(Integer teacherId) {
        List<Study> studies = studyDao.findAll();
        List<Signature> signatures = signatureDao.findByTeacherId(teacherId);


        return mergeSignaturesAndStudyList(teacherId, signatures, studies);
    }

    public Signature getByTeacherIdAndStudyId(Integer teacherId, Integer studyId) {
        return signatureDao.findSignatureByTeacherIdAndStudyId(teacherId, studyId);
    }

    public List<Signature.SignatureGrader> getAllByGraderId(Integer graderId) {
        return signatureGraderDao.getAllByGraderId(graderId);
    }

    public List<Signature.SignatureGrader> getAllByGraderIdAndGraderDateBetween(Integer graderId, String startDate, String endDate) {
        return signatureGraderDao.getAllByGraderIdAndGraderDateBetween(graderId, startDate, endDate);
    }


    public List<Signature> getGeneralAssemblySignaturesByUserIdDateBetween(Integer teacherId, String startDate, String endDate) {
        List<Study> studies = studyDao.findAll();
        List<Signature> signatures =
            signatureDao.getSignaturesByTeacherIdAndGeneralAssemblyUpdatedDateBetween(teacherId, startDate, endDate);


        return mergeSignaturesAndStudyList(teacherId, signatures, studies);
    }

    public List<Signature> getByStudyId(Integer studyId) {
        return signatureDao.findByStudyId(studyId);
    }

    @Override
    public Signature save(Signature signature) {
        // Attempt to save a new signature but the teacher already has existing signature
        // Instead of throwing an error update the existing one
        if (signature != null && signature.getId() == null) {
            Signature existingSignature = signatureDao.findSignatureByTeacherIdAndStudyId(signature.getTeacherId(), signature.getStudyId());

            if (existingSignature != null) {
                signature.setId(existingSignature.getId());
                signature.setCreatedDate(existingSignature.getCreatedDate());
                signature.setUpdatedDate(new Date());
                signature.setGeneralAssembly(existingSignature.getGeneralAssembly() + signature.getGeneralAssembly());
                signature.setReady(existingSignature.getReady() + signature.getReady());
                signature.setTeaching(existingSignature.getTeaching() + signature.getTeaching());
                signature.setGeneralAssemblyUpdatedDate(signature.getGeneralAssemblyUpdatedDate() != null ? signature.getGeneralAssemblyUpdatedDate() : existingSignature.getGeneralAssemblyUpdatedDate());
                signature.setReadyUpdatedDate(signature.getReadyUpdatedDate() != null ? signature.getReadyUpdatedDate() : existingSignature.getReadyUpdatedDate());
                signature.setTeachingUpdatedDate(signature.getTeachingUpdatedDate() != null ? signature.getTeachingUpdatedDate() : existingSignature.getTeachingUpdatedDate());
            }
        }

        Signature updatedSignature = super.save(signature);
        evictAllCache();
        return updatedSignature;
    }

    private List<Signature.SignatureGeneralAssemblyMovementReport> getGeneralAssemblyMovementSignatureReport(Member[] members, List<Signature.SignatureGeneralAssemblyMovement> signatureGeneralAssemblyMovements) {
        Map<Integer, Signature.SignatureGeneralAssemblyMovement> signatureGeneralAssemblyMovementMap = new HashMap<>();

        for (Signature.SignatureGeneralAssemblyMovement signatureGeneralAssemblyMovement : signatureGeneralAssemblyMovements) {
            signatureGeneralAssemblyMovementMap.put(signatureGeneralAssemblyMovement.getTeacherId(), signatureGeneralAssemblyMovement);
        }

        List<Signature.SignatureGeneralAssemblyMovementReport> signatureGeneralAssemblyMovementReports = new ArrayList<>();

        for (Member member : members) {
            // Only include members with a report in the response
            if (signatureGeneralAssemblyMovementMap.containsKey(member.getId())) {
                signatureGeneralAssemblyMovementReports.add(new Signature.SignatureGeneralAssemblyMovementReport(signatureGeneralAssemblyMovementMap.get(member.getId()), member));
            }
        }

        return signatureGeneralAssemblyMovementReports;
    }

    public List<Signature.SignatureDisplay> getTeacherCompletedSignatures(HttpServletRequest request) {
        List<Signature> signatures = signatureDao.findByGeneralAssemblyGreaterThanOrReadyGreaterThanOrTeachingGreaterThan(0, 0, 0);

        Member[] members = restService.getAllDisplayTeachers(request);

        return filterMemberSignatures(members, signatures);
    }

    public List<Signature.SignatureDisplay> getTeacherCompletedSignaturesByChurch(HttpServletRequest request, Integer churchId) {
        List<Signature> signatures = signatureDao.findByGeneralAssemblyGreaterThanOrReadyGreaterThanOrTeachingGreaterThan(0, 0, 0);

        Member[] members = restService.getAllDisplayChurchTeachers(request, churchId);

        return filterMemberSignatures(members, signatures);
    }

    public List<Signature.SignatureDisplay> getTeacherCompletedSignaturesByGroup(HttpServletRequest request, Integer groupId) {
        List<Signature> signatures = signatureDao.findByGeneralAssemblyGreaterThanOrReadyGreaterThanOrTeachingGreaterThan(0, 0, 0);

        Member[] members = restService.getAllDisplayGroupTeachers(request, groupId);

        return filterMemberSignatures(members, signatures);
    }

    public List<Signature.SignatureDisplay> getTeacherCompletedSignaturesByTeam(HttpServletRequest request, Integer teamId) {
        List<Signature> signatures = signatureDao.findByGeneralAssemblyGreaterThanOrReadyGreaterThanOrTeachingGreaterThan(0, 0, 0);

        Member[] members = restService.getAllDisplayTeamTeachers(request, teamId);

        return filterMemberSignatures(members, signatures);
    }

    private List<Signature.SignatureDisplay> filterMemberSignatures(Member[] members, List<Signature> signatures) {
        Map<Integer, Member> membersMap = new HashMap<>();

        for (Member member : members) {
            membersMap.put(member.getId(), member);
        }

        return signatures
            .stream()
            .filter(signature -> membersMap.containsKey(signature.getTeacherId()))
            .map(signature -> new Signature.SignatureDisplay(signature, membersMap.get(signature.getTeacherId())))
            .collect(Collectors.toList());
    }

    private List<Signature> mergeSignaturesAndStudyList(Integer teacherId, List<Signature> signatures, List<Study> studies) {
        List<Signature> teacherSignatures = new ArrayList<>();

        //if they are the same size then this member has all signatures
        if (signatures.size() == studies.size()) return signatures;

        //merge the studies and signatures to create a list of all chapters and show if the member has gotten the signature or not;
        for (Study study : studies) {

            //check if the teacher already has a signature for this sermon
            Optional<Signature> signatureOptional = signatures.stream().filter(s -> s.getStudyId().equals(study.getId())).findFirst();
            if (signatureOptional.isPresent()) {
                teacherSignatures.add(signatureOptional.get());
            }

            // If no signature then add as blank
            else {
                Signature signature = new Signature();
                signature.setStudy(study);
                signature.setGeneralAssembly(0);
                signature.setReady(0);
                signature.setTeacherId(teacherId);
                signature.setTeaching(0);
                teacherSignatures.add(signature);
            }
        }

        return teacherSignatures;
    }

    @CacheEvict(
        cacheNames = {
            "general-assembly-signatures-cache-user",
            "movement-general-assembly-signature-cache-all",
            "movement-general-assembly-signature-cache-church",
            "movement-general-assembly-signature-cache-group",
            "movement-general-assembly-signature-cache-team",
            "movement-general-assembly-signature-cache-user",
            "signatures-grader-cache-user"
        },
        allEntries = true
    )
    public void evictAllCache() {
    }
}
