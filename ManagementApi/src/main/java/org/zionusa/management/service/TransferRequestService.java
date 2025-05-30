package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.dao.TransferRequestDao;
import org.zionusa.management.dao.UserDao;
import org.zionusa.management.domain.TransferRequest;
import org.zionusa.management.domain.User;
import org.zionusa.management.enums.ETransferRequestStatus;
import org.zionusa.management.exception.NotFoundException;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransferRequestService extends BaseService<TransferRequest, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(TransferRequestService.class);

    private final TransferRequestDao transferRequestDao;
    private final UserDao userDao;
    private final UserService userService;

    @Value("${dashboard.url}")
    String dashboardUrl;

    @Autowired
    public TransferRequestService(TransferRequestDao transferRequestDao, UserDao userDao, UserService userService) {
        super(transferRequestDao, logger, TransferRequest.class);
        this.transferRequestDao = transferRequestDao;
        this.userDao = userDao;
        this.userService = userService;
    }

    @PostAuthorize("hasAuthority('Admin')")
    public List<TransferRequest> getAllTransferRequests(String status) {

        if (status != null) {
            List<TransferRequest> allTransferRequests = transferRequestDao.findAll();
            List<TransferRequest> filteredTransferRequests = new ArrayList<>();

            for (TransferRequest transferRequest : allTransferRequests) {
                if (transferRequest.getRequestStatus().equals(status))
                    filteredTransferRequests.add(transferRequest);
            }
            return filteredTransferRequests;
        }
        return transferRequestDao.findAll();

    }

    public List<TransferRequest> getTransferRequestsBetweenDate(Date startDate, Date endDate) {
        return transferRequestDao.getTransferRequestsByRequestDateBetween(startDate, endDate);
    }


    @PostAuthorize("@authenticatedUserService.canAccessUser(principal, #userId)")
    public List<TransferRequest> getUserTransferRequests(Integer userId, String status) {

        if (status != null) {
            List<TransferRequest> allTransferRequests = transferRequestDao.getTransferRequestsByUserId(userId);
            List<TransferRequest> filteredTransferRequests = new ArrayList<>();

            for (TransferRequest transferRequest : allTransferRequests) {
                if (transferRequest.getRequestStatus().equals(status))
                    filteredTransferRequests.add(transferRequest);
            }
            return filteredTransferRequests;
        }

        return transferRequestDao.getTransferRequestsByUserId(userId);
    }

    public List<TransferRequest> getTeamTransferRequests(Integer teamId, String status) {

        if (status != null) {
            List<TransferRequest> allTransferRequests = transferRequestDao.getTransferRequestsByNewTeamId(teamId);
            List<TransferRequest> filteredTransferRequests = new ArrayList<>();

            for (TransferRequest transferRequest : allTransferRequests) {
                if (transferRequest.getRequestStatus().equals(status))
                    filteredTransferRequests.add(transferRequest);
            }
            return filteredTransferRequests;
        }

        return transferRequestDao.getTransferRequestsByNewTeamId(teamId);
    }


    @PreAuthorize("!hasAuthority('Member') && !hasAuthority('Team') && @authenticatedUserService.canAccessGroup(principal, #groupId)")
    public List<TransferRequest> getGroupTransferRequests(Integer groupId, String status) {

        if (status != null) {
            List<TransferRequest> allTransferRequests = transferRequestDao.getTransferRequestsByNewGroupId(groupId);
            List<TransferRequest> filteredTransferRequests = new ArrayList<>();

            for (TransferRequest transferRequest : allTransferRequests) {
                if (transferRequest.getRequestStatus().equals(status))
                    filteredTransferRequests.add(transferRequest);
            }
            return filteredTransferRequests;
        }
        return transferRequestDao.getTransferRequestsByNewGroupId(groupId);
    }

    @PreAuthorize("!hasAuthority('Member') && !hasAuthority('Group') && @authenticatedUserService.canAccessChurch(principal, #churchId)")
    public List<TransferRequest> getBranchTransferRequests(Integer churchId, String status) {
        if (status != null) {
            List<TransferRequest> allTransferRequests = transferRequestDao.getTransferRequestsByNewChurchId(churchId);
            List<TransferRequest> filteredTransferRequests = new ArrayList<>();

            for (TransferRequest transferRequest : allTransferRequests) {
                if (transferRequest.getRequestStatus().equals(status))
                    filteredTransferRequests.add(transferRequest);
            }
            return filteredTransferRequests;
        }
        return transferRequestDao.getTransferRequestsByNewChurchId(churchId);
    }

    @PreAuthorize("@authenticatedUserService.canModifyTransferRequest(principal, #transferRequest)")
    public TransferRequest save(TransferRequest transferRequest) {
        logger.info("Starting a transfer request for {} (UserId: {})", transferRequest.getName(), transferRequest.getUserId());

        transferRequest.setRequestStatus(ETransferRequestStatus.PENDING.getValue());

        TransferRequest result = transferRequestDao.save(transferRequest);

        //No longer emailing, approve request right away
        approve(result.getId());

        return result;
    }

    @PreAuthorize("@authenticatedUserService.canProcessTransfer(principal, #id)")
    public boolean approve(Integer id) {
        Optional<TransferRequest> transferRequestOptional = transferRequestDao.findById(id);

        if (!transferRequestOptional.isPresent())
            throw new NotFoundException("The transfer request was not found, therefore cannot approve member");

        TransferRequest transferRequest = transferRequestOptional.get();

        Optional<User> userOptional = userDao.findById(transferRequest.getUserId());

        if (!userOptional.isPresent())
            throw new NotFoundException(transferRequest.getName() + " was not found, cannot transfer them");

        User user = userOptional.get();
        user.setTeamId(transferRequest.getNewTeamId());
        logger.info("UserId: {}", user.getId());

        // If the team id of the user was changed also update the group and team id
        User result = userService.internalSaveUser(user);

        //if change success, update table
        if (result.getTeamId() == transferRequest.getNewTeamId()) {
            Instant currentInstant = LocalDateTime.now().atZone(EZoneId.NEW_YORK.getValue()).toInstant();

            transferRequest.setRequestStatus(ETransferRequestStatus.APPROVED.getValue());
            transferRequestDao.save(transferRequest);
            logger.info("Transfer request completed for {} (UserId: {})", transferRequest.getName(), transferRequest.getUserId());
            return true;
        }

        logger.info("Transfer request failed for {} (UserId: {})", transferRequest.getName(), transferRequest.getUserId());
        return false;
    }

    @PreAuthorize("@authenticatedUserService.canProcessTransfer(principal, #id)")
    public boolean deny(Integer id) {
        Optional<TransferRequest> transferRequestOptional = transferRequestDao.findById(id);

        if (!transferRequestOptional.isPresent())
            throw new NotFoundException("The transfer request was not found, therefore cannot deny it");

        TransferRequest transferRequest = transferRequestOptional.get();
        Instant currentInstant = LocalDateTime.now().atZone(EZoneId.NEW_YORK.getValue()).toInstant();

        transferRequest.setRequestStatus(ETransferRequestStatus.DENIED.getValue());
        transferRequestDao.save(transferRequest);

        return true;
    }

    private String createHTMLEmail(String message, TransferRequest transferRequest) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");

        StringBuilder sb = new StringBuilder();
        sb.append("<div style='width:100%;text-align:left;margin:1%'>");
        sb.append("<h1>Shout of Joshua User Transfer Request</h1>");
        sb.append("<div>").append(message).append("</div>");
        sb.append("<br/>");
        sb.append("<div><ul style='list-style:none;padding-left:0;font-size:large;'>");
        sb.append("<li><label style='font-weight:bold;'>Member:</label> ").append(transferRequest.getName()).append("</li>");
        sb.append("<li><label style='font-weight:bold;'>Requested Transfer Date:</label> ").append(sdf.format(transferRequest.getRequestDate())).append("</li>");
        sb.append("<li><label style='font-weight:bold;'>Original Church:</label> ").append(transferRequest.getCurrentChurchName()).append("</li>");
        sb.append("<li><label style='font-weight:bold;'>Original Group:</label> ").append(transferRequest.getCurrentGroupName()).append("</li>");
        sb.append("<li><label style='font-weight:bold;color:blue;'>New Church:</label> ").append(transferRequest.getNewChurchName()).append("</li>");
        sb.append("<li><label style='font-weight:bold;color:blue;'>New Group:</label> ").append(transferRequest.getNewGroupName()).append("</li>");
        sb.append("</ul></div>");
        sb.append("<br/>");
        sb.append("<div style='display:flex;justify-content:center'>");
        sb.append("<a style='background-color:#333CFF;color:white;padding:.5em;border:1px solid #333CFF;border-radius:3px;font-size:18px;text-decoration:none' ");
        sb.append("href='").append(dashboardUrl).append("'>");
        sb.append("View Transfer Request").append("</a>");
        sb.append("</div>");
        sb.append("</div>");

        return sb.toString();
    }
}
