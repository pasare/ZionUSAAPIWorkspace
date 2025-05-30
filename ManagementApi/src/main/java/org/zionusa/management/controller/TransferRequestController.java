package org.zionusa.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.TransferRequest;
import org.zionusa.management.enums.ETransferRequestStatus;
import org.zionusa.management.service.TransferRequestService;

import java.util.List;

@RestController
@RequestMapping("/transfer-requests")
public class TransferRequestController extends BaseController<TransferRequest, Integer> {

    private final TransferRequestService transferRequestService;

    @Autowired
    public TransferRequestController(TransferRequestService transferRequestService) {
        super(transferRequestService);
        this.transferRequestService = transferRequestService;
    }

    /*** ALL Transfers Section **/

    @GetMapping(value = "/pending")
    List<TransferRequest> getAllPendingTransferRequests() {
        return transferRequestService.getAllTransferRequests(ETransferRequestStatus.PENDING.getValue());
    }

    @GetMapping(value = "/approved")
    List<TransferRequest> getAllApprovedTransferRequests() {
        return transferRequestService.getAllTransferRequests(ETransferRequestStatus.APPROVED.getValue());
    }

    @GetMapping(value = "/denied")
    List<TransferRequest> getAllDeniedTransferRequests() {
        return transferRequestService.getAllTransferRequests(ETransferRequestStatus.DENIED.getValue());
    }

    @GetMapping(value = "/users/{userId}")
    List<TransferRequest> getUserTransferRequests(@PathVariable Integer userId) {
        return transferRequestService.getUserTransferRequests(userId, null);
    }

    @GetMapping(value = "/users/{userId}/pending")
    List<TransferRequest> getPendingUserTransferRequests(@PathVariable Integer userId) {
        return transferRequestService.getUserTransferRequests(userId, ETransferRequestStatus.PENDING.getValue());
    }

    @GetMapping(value = "/users/{userId}/approved")
    List<TransferRequest> getApprovedUserTransferRequests(@PathVariable Integer userId) {
        return transferRequestService.getUserTransferRequests(userId, ETransferRequestStatus.APPROVED.getValue());
    }

    @GetMapping(value = "/users/{userId}/denied")
    List<TransferRequest> getDeniedUserTransferRequests(@PathVariable Integer userId) {
        return transferRequestService.getUserTransferRequests(userId, ETransferRequestStatus.DENIED.getValue());
    }

    @GetMapping(value = "/teams/{teamId}")
    List<TransferRequest> getTeamTransferRequests(@PathVariable Integer teamId) {
        return transferRequestService.getTeamTransferRequests(teamId, null);
    }

    @GetMapping(value = "/teams/{teamId}/pending")
    List<TransferRequest> getPendingTeamTransferRequests(@PathVariable Integer teamId) {
        return transferRequestService.getTeamTransferRequests(teamId, ETransferRequestStatus.PENDING.getValue());
    }

    @GetMapping(value = "/teams/{teamId}/approved")
    List<TransferRequest> getApprovedTeamTransferRequests(@PathVariable Integer teamId) {
        return transferRequestService.getTeamTransferRequests(teamId, ETransferRequestStatus.APPROVED.getValue());
    }

    @GetMapping(value = "/teams/{teamId}/denied")
    List<TransferRequest> getDeniedTeamTransferRequests(@PathVariable Integer teamId) {
        return transferRequestService.getTeamTransferRequests(teamId, ETransferRequestStatus.DENIED.getValue());
    }

    @GetMapping(value = "/groups/{groupId}")
    public List<TransferRequest> getGroupTransferRequests(@PathVariable Integer groupId) {
        return transferRequestService.getGroupTransferRequests(groupId, null);
    }

    @GetMapping(value = "/groups/{groupId}/pending")
    List<TransferRequest> getPendingGroupTransferRequests(@PathVariable Integer groupId) {
        return transferRequestService.getGroupTransferRequests(groupId, ETransferRequestStatus.PENDING.getValue());
    }

    @GetMapping(value = "/groups/{groupId}/approved")
    List<TransferRequest> getApprovedGroupTransferRequests(@PathVariable Integer groupId) {
        return transferRequestService.getGroupTransferRequests(groupId, ETransferRequestStatus.APPROVED.getValue());
    }

    @GetMapping(value = "/groups/{groupId}/denied")
    List<TransferRequest> getDeniedGroupTransferRequests(@PathVariable Integer groupId) {
        return transferRequestService.getGroupTransferRequests(groupId, ETransferRequestStatus.DENIED.getValue());
    }

    @GetMapping(value = "/branches/{branchId}")
    public List<TransferRequest> getBranchTransferRequests(@PathVariable Integer churchId) {
        return transferRequestService.getBranchTransferRequests(churchId, null);
    }

    @GetMapping(value = "/churches/{churchId}")
    public List<TransferRequest> getChurchTransferRequests(@PathVariable Integer churchId) {
        return transferRequestService.getBranchTransferRequests(churchId, null);
    }

    @GetMapping(value = "/churches/{churchId}/pending")
    List<TransferRequest> getPendingChurchTransferRequests(@PathVariable Integer churchId) {
        return transferRequestService.getBranchTransferRequests(churchId, ETransferRequestStatus.PENDING.getValue());
    }

    @GetMapping(value = "/churches/{churchId}/approved")
    List<TransferRequest> getApprovedChurchTransferRequests(@PathVariable Integer churchId) {
        return transferRequestService.getBranchTransferRequests(churchId, ETransferRequestStatus.APPROVED.getValue());
    }

    @GetMapping(value = "/churches/{churchId}/denied")
    List<TransferRequest> getDeniedChurchTransferRequests(@PathVariable Integer churchId) {
        return transferRequestService.getBranchTransferRequests(churchId, ETransferRequestStatus.DENIED.getValue());
    }

    @PutMapping(value = "/{id}/approve")
    public boolean approveTransferRequest(@PathVariable Integer id) {
        return transferRequestService.approve(id);
    }

    @PutMapping(value = "/{id}/deny")
    public boolean denyTransferRequest(@PathVariable Integer id) {
        return transferRequestService.deny(id);
    }
}
