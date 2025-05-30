package org.zionusa.event.domain.eventStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.event.domain.eventProposal.EventProposalsDao;
import org.zionusa.event.domain.eventProposal.EventProposal;

import java.util.Optional;

@Service
public class EventStatusService extends BaseService<EventStatus, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventStatusService.class);

    private final EventStatusDao eventStatusDao;
    private final EventProposalsDao eventProposalsDao;

    @Autowired
    public EventStatusService(EventStatusDao eventStatusDao, EventProposalsDao eventProposalsDao) {
        super(eventStatusDao, logger, EventStatus.class);
        this.eventStatusDao = eventStatusDao;
        this.eventProposalsDao = eventProposalsDao;
    }

    public EventStatus getByEventProposalId(Integer id) throws NotFoundException {
        Optional<EventStatus> eventStatusOptional = eventStatusDao.findByEventProposalId(id);

        if (!eventStatusOptional.isPresent())
            throw new NotFoundException("The event status could not be found in the system");

        EventStatus eventStatus = eventStatusOptional.get();

        logger.warn("Retrieving event status with event id : {} with id: {}", eventStatus.getEventProposalId(), eventStatus.getId());
        return eventStatus;
    }

    public EventStatus createEventStatus(EventProposal eventProposal) {
        EventStatus eventStatus = new EventStatus(eventProposal);

        EventStatus returnedEventStatus = eventStatusDao.save(eventStatus);

        //update the eventProposal with its new workflow
        eventProposal.setEventStatusId(eventStatus.getId());
        eventProposalsDao.save(eventProposal);

        return returnedEventStatus;
    }

    public EventStatus beginApprovalProcess(EventProposal eventProposal) {
        return null;
    }

    public EventStatus markEventFinalized(EventProposal eventProposal) {
        return null;
    }

    public EventStatus markEventManagerApproved(EventProposal eventProposal) {
        return null;
    }

    public EventStatus markEventAdminApproved(EventProposal eventProposal) {
        return null;
    }

    public EventStatus markEventGaSubmitted(EventProposal eventProposal) {
        return null;
    }

    public EventStatus markEventGaApproved(EventProposal eventProposal) {
        return null;
    }
}
