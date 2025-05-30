package org.zionusa.event.domain.eventvolunteer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.event.domain.resultsSurvey.ResultsSurveysDao;
import org.zionusa.event.domain.resultsSurvey.ResultsSurvey;
import org.zionusa.event.domain.VolunteerType;

import java.util.List;
import java.util.Optional;

@Service
public class EventVolunteerManagementService extends BaseService<EventVolunteerManagement, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventVolunteerManagementService.class);
    private final EventVolunteerManagementDao eventVolunteerManagementDao;
    private final ResultsSurveysDao resultsSurveysDao;

    @Autowired
    public EventVolunteerManagementService(EventVolunteerManagementDao eventVolunteerManagementDao,
                                           ResultsSurveysDao resultsSurveysDao) {
        super(eventVolunteerManagementDao, logger, EventVolunteerManagement.class);
        this.eventVolunteerManagementDao = eventVolunteerManagementDao;
        this.resultsSurveysDao = resultsSurveysDao;
    }

    public List<EventVolunteerManagement> getVolunteersByEventProposal(Integer eventProposalId, Boolean isForResultsSurvey) {
        return isForResultsSurvey == null
            ? eventVolunteerManagementDao.getEventVolunteerManagementsByEventProposalId(eventProposalId)
            : eventVolunteerManagementDao.getEventVolunteerManagementsByEventProposalIdAndIsForResultsSurvey(eventProposalId, isForResultsSurvey);
    }

    public EventVolunteerManagementTotals getVolunteerTotalsByEventProposal(Integer eventProposalId, Boolean isForResultsSurvey) {
        List<EventVolunteerManagement> eventVolunteerManagements = getVolunteersByEventProposal(eventProposalId, isForResultsSurvey);

        EventVolunteerManagementTotals totals = new EventVolunteerManagementTotals();
        totals.setEventProposalId(eventProposalId);
        totals.setIsForResultsSurvey(isForResultsSurvey);

        for (EventVolunteerManagement current : eventVolunteerManagements) {
            if (current.getType() == VolunteerType.CONTACT) {
                totals.setFemaleAdultContacts(totals.getFemaleAdultContacts() + current.getFemaleAdults());
                totals.setFemaleCollegeStudentContacts(totals.getFemaleCollegeStudentContacts() + current.getFemaleCollegeStudents());
                totals.setFemaleTeenagerContacts(totals.getFemaleTeenagerContacts() + current.getFemaleTeenagers());
                totals.setFemaleYoungAdultContacts(totals.getFemaleYoungAdultContacts() + current.getFemaleYoungAdults());
                totals.setMaleAdultContacts(totals.getMaleAdultContacts() + current.getMaleAdults());
                totals.setMaleCollegeStudentContacts(totals.getMaleCollegeStudentContacts() + current.getMaleCollegeStudents());
                totals.setMaleTeenagerContacts(totals.getMaleTeenagerContacts() + current.getMaleTeenagers());
                totals.setMaleYoungAdultContacts(totals.getMaleYoungAdultContacts() + current.getMaleYoungAdults());
            } else if (current.getType() == VolunteerType.MEMBER) {
                totals.setFemaleAdultMembers(totals.getFemaleAdultMembers() + current.getFemaleAdults());
                totals.setFemaleCollegeStudentMembers(totals.getFemaleCollegeStudentMembers() + current.getFemaleCollegeStudents());
                totals.setFemaleTeenagerMembers(totals.getFemaleTeenagerMembers() + current.getFemaleTeenagers());
                totals.setFemaleYoungAdultMembers(totals.getFemaleYoungAdultMembers() + current.getFemaleYoungAdults());
                totals.setMaleAdultMembers(totals.getMaleAdultMembers() + current.getMaleAdults());
                totals.setMaleCollegeStudentMembers(totals.getMaleCollegeStudentMembers() + current.getMaleCollegeStudents());
                totals.setMaleTeenagerMembers(totals.getMaleTeenagerMembers() + current.getMaleTeenagers());
                totals.setMaleYoungAdultMembers(totals.getMaleYoungAdultMembers() + current.getMaleYoungAdults());
            } else if (current.getType() == VolunteerType.VIP) {
                totals.setFemaleAdultVips(totals.getFemaleAdultVips() + current.getFemaleAdults());
                totals.setFemaleCollegeStudentVips(totals.getFemaleCollegeStudentVips() + current.getFemaleCollegeStudents());
                totals.setFemaleTeenagerVips(totals.getFemaleTeenagerVips() + current.getFemaleTeenagers());
                totals.setFemaleYoungAdultVips(totals.getFemaleYoungAdultVips() + current.getFemaleYoungAdults());
                totals.setMaleAdultVips(totals.getMaleAdultVips() + current.getMaleAdults());
                totals.setMaleCollegeStudentVips(totals.getMaleCollegeStudentVips() + current.getMaleCollegeStudents());
                totals.setMaleTeenagerVips(totals.getMaleTeenagerVips() + current.getMaleTeenagers());
                totals.setMaleYoungAdultVips(totals.getMaleYoungAdultVips() + current.getMaleYoungAdults());
            }
        }

        // Totals
        totals.setFemaleAdultTotals(totals.getFemaleAdultVips() + totals.getFemaleAdultMembers() + totals.getFemaleAdultContacts());
        totals.setFemaleCollegeStudentTotals(totals.getFemaleCollegeStudentVips() + totals.getFemaleCollegeStudentMembers() + totals.getFemaleCollegeStudentContacts());
        totals.setFemaleTeenagerTotals(totals.getFemaleTeenagerVips() + totals.getFemaleTeenagerMembers() + totals.getFemaleTeenagerContacts());
        totals.setFemaleYoungAdultTotals(totals.getFemaleYoungAdultVips() + totals.getFemaleYoungAdultMembers() + totals.getFemaleYoungAdultContacts());
        totals.setMaleAdultTotals(totals.getMaleAdultVips() + totals.getMaleAdultMembers() + totals.getMaleAdultContacts());
        totals.setMaleCollegeStudentTotals(totals.getMaleCollegeStudentVips() + totals.getMaleCollegeStudentMembers() + totals.getMaleCollegeStudentContacts());
        totals.setMaleTeenagerTotals(totals.getMaleTeenagerVips() + totals.getMaleTeenagerMembers() + totals.getMaleTeenagerContacts());
        totals.setMaleYoungAdultTotals(totals.getMaleYoungAdultVips() + totals.getMaleYoungAdultMembers() + totals.getMaleYoungAdultContacts());

        totals.setAdults(totals.getFemaleAdultTotals() + totals.getMaleAdultTotals());
        totals.setCollegeStudents(totals.getFemaleCollegeStudentTotals() + totals.getMaleCollegeStudentTotals());
        totals.setTeenagers(totals.getFemaleTeenagerTotals() + totals.getMaleTeenagerTotals());
        totals.setYoungAdults(totals.getFemaleYoungAdultTotals() + totals.getMaleYoungAdultTotals());

        totals.setFemaleContacts(totals.getFemaleAdultContacts() + totals.getFemaleCollegeStudentContacts() +
            totals.getFemaleTeenagerContacts() + totals.getFemaleYoungAdultContacts());
        totals.setFemaleMembers(totals.getFemaleAdultMembers() + totals.getFemaleCollegeStudentMembers() +
            totals.getFemaleTeenagerMembers() + totals.getFemaleYoungAdultMembers());
        totals.setFemaleVips(totals.getFemaleAdultVips() + totals.getFemaleCollegeStudentVips() +
            totals.getFemaleTeenagerVips() + totals.getFemaleYoungAdultVips());

        totals.setMaleContacts(totals.getMaleAdultContacts() + totals.getMaleCollegeStudentContacts()
            + totals.getMaleTeenagerContacts() + totals.getMaleYoungAdultContacts());
        totals.setMaleMembers(totals.getMaleAdultMembers() + totals.getMaleCollegeStudentMembers()
            + totals.getMaleTeenagerMembers() + totals.getMaleYoungAdultMembers());
        totals.setMaleVips(totals.getMaleAdultVips() + totals.getMaleCollegeStudentVips()
            + totals.getMaleTeenagerVips() + totals.getMaleYoungAdultVips());

        totals.setContacts(totals.getFemaleAdultContacts() + totals.getFemaleCollegeStudentContacts() +
            totals.getFemaleTeenagerContacts() + totals.getFemaleYoungAdultContacts() +
            totals.getMaleAdultContacts() + totals.getMaleCollegeStudentContacts()
            + totals.getMaleTeenagerContacts() + totals.getMaleYoungAdultContacts()
        );
        totals.setMembers(totals.getFemaleAdultMembers() + totals.getFemaleCollegeStudentMembers() +
            totals.getFemaleTeenagerMembers() + totals.getFemaleYoungAdultMembers() +
            totals.getMaleAdultMembers() + totals.getMaleCollegeStudentMembers()
            + totals.getMaleTeenagerMembers() + totals.getMaleYoungAdultMembers()
        );
        totals.setVips(totals.getFemaleAdultVips() + totals.getFemaleCollegeStudentVips() +
            totals.getFemaleTeenagerVips() + totals.getFemaleYoungAdultVips() +
            totals.getMaleAdultVips() + totals.getMaleCollegeStudentVips()
            + totals.getMaleTeenagerVips() + totals.getMaleYoungAdultVips()
        );
        totals.setTotal(totals.getContacts() + totals.getMembers() + totals.getVips());

        return totals;
    }

    public EventVolunteerManagementTotals getVolunteerTotalsByResultsSurvey(Integer resultsSurveyId, Boolean isForResultsSurvey) {
        Optional<ResultsSurvey> resultsSurveyOptional = resultsSurveysDao.findById(resultsSurveyId);

        if (resultsSurveyOptional.isPresent()) {
            ResultsSurvey resultsSurvey = resultsSurveyOptional.get();

            if (resultsSurvey.getEventProposalId() != null) {
                EventVolunteerManagementTotals totals = getVolunteerTotalsByEventProposal(resultsSurvey.getEventProposalId(), isForResultsSurvey);
                totals.setResultsSurveyId(resultsSurveyId);
                return totals;
            }
        }

        throw new NotFoundException();
    }

}
