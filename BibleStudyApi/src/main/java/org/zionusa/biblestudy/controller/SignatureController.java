package org.zionusa.biblestudy.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.Signature;
import org.zionusa.biblestudy.service.SignatureService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/signatures")
public class SignatureController extends BaseController<Signature, Integer> {

    private final SignatureService signatureService;

    @Autowired
    public SignatureController(SignatureService signatureService) {
        super(signatureService);
        this.signatureService = signatureService;
    }

    @GetMapping(value = "/cache/evict")
    @ApiOperation(value = "Force signatures/movement/ and signatures/report/ endpoints to evict cache")
    public void forceCacheEvict() {
        this.signatureService.forceCacheEvict();
    }

    @GetMapping(value = "/count")
    public Collection<Signature.SignatureCount> getSignatureCount() {
        return this.signatureService.getSignatureCount();
    }

    @PostMapping(value = "/evaluate")
    @ApiOperation(value = "Create a GA, R or T evaluation for another member")
    public void evaluateSignature(@Valid @RequestBody Signature.SignatureEvaluation signatureEvaluation) throws Exception {
        this.signatureService.evaluateSignature(signatureEvaluation);
    }

    @GetMapping("/grader/{userId}")
    @ApiOperation(value = "View the signatures a member has given")
    public List<Signature.SignatureGrader> getAllByGraderId(@PathVariable Integer userId) {
        return this.signatureService.getAllByGraderId(userId);
    }

    @GetMapping("/grader/{userId}/{startDate}/{endDate}")
    @ApiOperation(value = "View the signatures a member has given within the date range")
    public List<Signature.SignatureGrader> getAllByGraderIdAndGraderDateBetween(
        @PathVariable Integer userId,
        @PathVariable String startDate,
        @PathVariable String endDate) {
        return this.signatureService.getAllByGraderIdAndGraderDateBetween(userId, startDate, endDate);
    }

    @GetMapping(value = "/movement/general-assembly")
    @ApiOperation(value = "View a movement report of the top # user counts or all and completed general assembly signatures")
    public List<Signature.SignatureGeneralAssemblyMovementReport> getGeneralAssemblySignatureMovementTop300Reports(HttpServletRequest request, @RequestParam(required = false) Integer top) {
        return this.signatureService.getGeneralAssemblySignatureMovementTop300Reports(request, top);
    }

    @GetMapping(value = "/movement/general-assembly/church/{churchId}")
    @ApiOperation(value = "View a report of counts and completed general assembly signatures per member per church")
    public List<Signature.SignatureGeneralAssemblyMovementReport> getGeneralAssemblySignatureMovementReportByChurchId(HttpServletRequest request, @PathVariable Integer churchId) {
        return this.signatureService.getGeneralAssemblySignatureMovementReportByChurchId(request, churchId);
    }

    @GetMapping(value = "/movement/general-assembly/group/{groupId}")
    @ApiOperation(value = "View a report of counts and completed general assembly signatures per member per group")
    public List<Signature.SignatureGeneralAssemblyMovementReport> getGeneralAssemblySignatureMovementReportByGroupId(HttpServletRequest request, @PathVariable Integer groupId) {
        return this.signatureService.getGeneralAssemblySignatureMovementReportByGroupId(request, groupId);
    }

    @GetMapping(value = "/movement/general-assembly/team/{teamId}")
    @ApiOperation(value = "View a report of counts and completed general assembly signatures per member per team")
    public List<Signature.SignatureGeneralAssemblyMovementReport> getGeneralAssemblySignatureMovementReportByTeamId(HttpServletRequest request, @PathVariable Integer teamId) {
        return this.signatureService.getGeneralAssemblySignatureMovementReportByTeamId(request, teamId);
    }

    @GetMapping(value = "/movement/general-assembly/user/{userId}")
    @ApiOperation(value = "View a movement report of counts and completed general assembly signatures per member")
    public Signature.SignatureGeneralAssemblyMovementReport getGeneralAssemblyMovementSignatureReportByTeacherId(HttpServletRequest request, @PathVariable Integer userId) {
        return this.signatureService.getGeneralAssemblyMovementSignatureReportByTeacherId(request, userId);
    }

    @GetMapping("/teacher/{userId}/{startDate}/{endDate}")
    @ApiOperation(value = "View the signatures of a member when a general assembly signature is completed within " +
        "the date range")
    public List<Signature> getGeneralAssemblySignaturesByUserIdDateBetween(
        @PathVariable Integer userId,
        @PathVariable String startDate,
        @PathVariable String endDate) {
        return this.signatureService.getGeneralAssemblySignaturesByUserIdDateBetween(userId, startDate, endDate);
    }

    @GetMapping(value = "/study/{studyId}")
    public List<Signature> getSignaturesByStudyId(@PathVariable Integer studyId) {
        return this.signatureService.getByStudyId(studyId);
    }

    @GetMapping(value = "/teacher/{teacherId}")
    public List<Signature> getSignaturesByTeacherId(@PathVariable Integer teacherId) {
        return this.signatureService.getByTeacherId(teacherId);
    }

    @GetMapping(value = "/teacher/{teacherId}/study/{studyId}")
    public Signature getSignatureByTeacherIdAndStudyId(@PathVariable Integer teacherId, @PathVariable Integer studyId) {
        return this.signatureService.getByTeacherIdAndStudyId(teacherId, studyId);
    }

    @GetMapping(value = "/teacher/completed")
    @ApiOperation(value = "View signatures completed for teachers")
    public List<Signature.SignatureDisplay> getTeacherCompletedSignatures(HttpServletRequest request) {
        return this.signatureService.getTeacherCompletedSignatures(request);
    }

    @GetMapping(value = "/teacher/completed/church/{churchId}")
    @ApiOperation(value = "View signatures completed for teachers by church")
    public List<Signature.SignatureDisplay> getTeacherCompletedSignaturesByChurch(HttpServletRequest request, @PathVariable Integer churchId) {
        return this.signatureService.getTeacherCompletedSignaturesByChurch(request, churchId);
    }

    @GetMapping(value = "/teacher/completed/group/{groupId}")
    @ApiOperation(value = "View signatures completed for teachers by group")
    public List<Signature.SignatureDisplay> getTeacherCompletedSignaturesByGroup(HttpServletRequest request, @PathVariable Integer groupId) {
        return this.signatureService.getTeacherCompletedSignaturesByGroup(request, groupId);
    }

    @GetMapping(value = "/teacher/completed/group/{teamId}")
    @ApiOperation(value = "View signatures completed for teachers by team")
    public List<Signature.SignatureDisplay> getTeacherCompletedSignaturesByTeam(HttpServletRequest request, @PathVariable Integer teamId) {
        return this.signatureService.getTeacherCompletedSignaturesByTeam(request, teamId);
    }
}
