package org.zionusa.admin.service;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.VisitingTripDao;
import org.zionusa.admin.domain.VisitingTrip;
import org.zionusa.admin.domain.VisitingTripApprovalNotes;
import org.zionusa.admin.domain.VisitingTripStatus;
import org.zionusa.base.enums.EUserRole;
import org.zionusa.base.util.auth.AuthenticatedUser;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class VisitingTripServiceWorkflow {
    private static final Logger logger = LoggerFactory.getLogger(VisitingTripServiceWorkflow.class);

    private final VisitingTripDao visitingTripDao;

    @Autowired
    public VisitingTripServiceWorkflow(VisitingTripDao visitingTripDao) {
        this.visitingTripDao = visitingTripDao;
    }

    public VisitingTrip processApproval(HttpServletRequest request, VisitingTrip visitingTrip, VisitingTripApprovalNotes visitingTripApprovalNotes) throws NotFoundException {
        if (visitingTrip != null) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (validateAdmin(authenticatedUser) || validateOverseer(authenticatedUser)) {
                VisitingTripStatus visitingTripStatus = getVisitingTripStatus(visitingTrip);

                visitingTripStatus.setApproved(true);
                visitingTripStatus.setApprovedDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
                visitingTripStatus.setApprovedNotes(visitingTripApprovalNotes.getNotes());
                visitingTripStatus.setSubmitted(true);

                visitingTrip.setVisitingTripStatus(visitingTripStatus);
                VisitingTrip returnedVisitingTrip = visitingTripDao.save(visitingTrip);
                logger.warn("Approved visitingTrip id {}", visitingTrip.getId());
                return returnedVisitingTrip;
            } else {
                logger.warn("{} cannot approve visiting trips because not have the role of Admin or Overseer", authenticatedUser.getDisplayName());
            }
        }
        String errorMessage = "The approval process was not started, either the user does not have the correct role for the next step of approval, or there was an internal failure.";
        logger.error(errorMessage);
        throw new NotFoundException(errorMessage);
    }

    public VisitingTrip processDenial(HttpServletRequest request, VisitingTrip visitingTrip, VisitingTripApprovalNotes visitingTripApprovalNotes) {
        if (visitingTrip != null) {
            VisitingTripStatus visitingTripStatus = getVisitingTripStatus(visitingTrip);

            visitingTripStatus.setApproved(false);
            visitingTripStatus.setApprovedDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));
            visitingTripStatus.setApprovedNotes(visitingTripApprovalNotes.getNotes());
            visitingTripStatus.setSubmitted(false);

            visitingTrip.setVisitingTripStatus(visitingTripStatus);
            VisitingTrip returnedVisitingTrip = visitingTripDao.save(visitingTrip);
            logger.warn("Denied visitingTrip id {}", visitingTrip.getId());
            return returnedVisitingTrip;
        }
        logger.error("The approval process was not started, either the user does not have the correct role for the next step of approval, or there was an internal failure.");
        return null;
    }

    public VisitingTrip processSubmission(HttpServletRequest request, VisitingTrip visitingTrip) {
        if (visitingTrip != null) {
            VisitingTripStatus visitingTripStatus = getVisitingTripStatus(visitingTrip);

            visitingTripStatus.setApproved(false);
            visitingTripStatus.setApprovedDate(null);
            visitingTripStatus.setApprovedNotes("");
            visitingTripStatus.setSubmitted(true);
            visitingTripStatus.setSubmittedDate(LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE));

            visitingTrip.setArchived(false);
            visitingTrip.setVisitingTripStatus(visitingTripStatus);
            VisitingTrip returnedVisitingTrip = visitingTripDao.save(visitingTrip);
            logger.warn("Submitted visitingTrip id {}", visitingTrip.getId());
            return returnedVisitingTrip;
        }
        logger.error("The approval process was not started, either the user does not have the correct role for the next step of approval, or there was an internal failure.");
        return null;
    }

    private VisitingTripStatus getVisitingTripStatus(VisitingTrip visitingTrip) {
        return visitingTrip.getVisitingTripStatus() != null
                ? visitingTrip.getVisitingTripStatus()
                : new VisitingTripStatus(visitingTrip);
    }

    private boolean validateAdmin(AuthenticatedUser authenticatedUser) {
        return EUserRole.ADMIN.is(authenticatedUser.getRole());
    }

    private boolean validateOverseer(AuthenticatedUser authenticatedUser) {
        return EUserRole.OVERSEER.is(authenticatedUser.getRole());
    }
}
