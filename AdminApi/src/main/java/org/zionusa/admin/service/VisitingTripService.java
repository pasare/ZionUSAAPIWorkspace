package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.VisitingTripDao;
import org.zionusa.admin.domain.VisitingTrip;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VisitingTripService extends BaseService<VisitingTrip, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(VisitingTripService.class);

    private final VisitingTripDao visitingTripDao;

    @Autowired
    public VisitingTripService(VisitingTripDao visitingTripDao) {
        super(visitingTripDao, logger, VisitingTrip.class);
        this.visitingTripDao = visitingTripDao;
    }

    @Override
    public VisitingTrip save(VisitingTrip visitingTrip) {
        if (visitingTrip.getId() != null) {
            logger.warn("Attempting to save visiting trip id {}", visitingTrip.getId());
            Optional<VisitingTrip> visitingTripOptional = visitingTripDao.findById(visitingTrip.getId());

            // Set the status of the visiting trip to what was previously in the database or to a new status
            if (visitingTripOptional.isPresent() && visitingTripOptional.get().getVisitingTripStatus() != null) {
                visitingTrip.setVisitingTripStatus(visitingTripOptional.get().getVisitingTripStatus());
            }
        } else {
            logger.warn("Attempting to save new visiting trip");
        }

        VisitingTrip returnedVisitingTrip = visitingTripDao.save(visitingTrip);
        logger.warn("Saved visiting trip id {}", visitingTrip.getId());
        return returnedVisitingTrip;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        Optional<VisitingTrip> visitingTripOptional = visitingTripDao.findById(id);

        if (!visitingTripOptional.isPresent())
            throw new NotFoundException("Cannot delete/archive visiting trip id " + id + " because it does not exist");

        VisitingTrip visitingTrip = visitingTripOptional.get();

        // this event was already finalized so we should archive it
        if (visitingTrip.getVisitingTripStatus().getApproved()) {
            visitingTrip.setArchived(true);
            visitingTripDao.save(visitingTrip);
            logger.warn("Archived visitingTrip id {}", visitingTrip.getId());
        } else {
            // this event is still a draft so we can delete it
            visitingTripDao.delete(visitingTrip);
            logger.warn("Deleted visiting trip id {}", visitingTrip.getId());
        }
    }

    public List<VisitingTrip> getByHomeZionId(String id) {
        List<VisitingTrip> visitingTrips = new ArrayList<>();

        try {
            int churchId = Integer.parseInt(id.trim());
            visitingTrips.addAll(visitingTripDao.getByHomeZionId(churchId));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("One of the provided home zion id's is invalid");
        }

        logger.warn("Retrieved visiting trips for home zion {}", id);

        return visitingTrips;
    }

    public List<VisitingTrip> getByHomeZionIds(String ids) {
        List<VisitingTrip> users = new ArrayList<>();

        for (String id : ids.split(",")) {
            try {
                int churchId = Integer.parseInt(id.trim());
                users.addAll(visitingTripDao.getByHomeZionId(churchId));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("One of the provided id's is invalid");
            }
        }
        logger.warn("Retrieved visiting trips for home zion {}", ids);

        return users;
    }

    public List<VisitingTrip> getByVisitingZionId(String id) {
        List<VisitingTrip> visitingTrips = new ArrayList<>();

        try {
            int churchId = Integer.parseInt(id.trim());
            visitingTrips.addAll(visitingTripDao.getByVisitingZionId(churchId));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("One of the provided visiting zion id's is invalid");
        }

        logger.warn("Retrieved visiting trips for visiting zion {}", id);

        return visitingTrips;
    }

    public List<VisitingTrip> getByVisitingZionIds(String ids) {
        List<VisitingTrip> users = new ArrayList<>();

        for (String id : ids.split(",")) {
            try {
                int churchId = Integer.parseInt(id.trim());
                users.addAll(visitingTripDao.getByVisitingZionId(churchId));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("One of the provided id's is invalid");
            }
        }
        logger.warn("Retrieved visiting trips for visiting zion {}", ids);

        return users;
    }
}
