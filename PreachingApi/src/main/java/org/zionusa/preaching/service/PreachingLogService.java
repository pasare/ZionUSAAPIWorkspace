package org.zionusa.preaching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.preaching.dao.PreachingLogDao;
import org.zionusa.preaching.dao.ReportDao;
import org.zionusa.preaching.domain.Partner;
import org.zionusa.preaching.domain.PreachingLog;
import org.zionusa.preaching.domain.Report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PreachingLogService extends BaseService<PreachingLog> {
    private static final Logger logger = LoggerFactory.getLogger(PreachingLogService.class);

    private final PreachingLogDao preachingLogDao;

    private final ReportDao reportDao;

    @Autowired
    public PreachingLogService(PreachingLogDao preachingLogDao, ReportDao reportDao) {
        super(preachingLogDao, logger, PreachingLog.class);
        this.preachingLogDao = preachingLogDao;
        this.reportDao = reportDao;
    }

    public List<PreachingLog> getByUserId(Integer userId) {
        return preachingLogDao.getPreachingLogsByUserId1OrUserId2OrUserId3(userId, userId, userId);
    }

    public List<PreachingLog> getPreachingLogsByUserIdAndDate(Integer userId, String startDate, String endDate) {
        return preachingLogDao.getByUserIdAndDate(userId, startDate, endDate);
    }

    public PreachingLog createHideFromViewLog(PreachingLog preachingLog) {
        return null;
    }

    @Override
    public PreachingLog save(PreachingLog preachingLog) {

        //update the report for all partners involved, also set the user ids
        //partner object must be passed in by the UI this is because we need groupId, teamId, etc
        List<Partner> partners = new ArrayList<>();
        if (preachingLog.getPartner1() != null) {
            partners.add(preachingLog.getPartner1());
            preachingLog.setUserId1(preachingLog.getPartner1().getId());
        }
        if (preachingLog.getPartner2() != null) {
            partners.add(preachingLog.getPartner2());
            preachingLog.setUserId2(preachingLog.getPartner2().getId());
        }
        if (preachingLog.getPartner3() != null) {
            partners.add(preachingLog.getPartner3());
            preachingLog.setUserId3(preachingLog.getPartner3().getId());
        }

        for (Partner partner : partners) {
            Report report = createOrUpdateUserReport(preachingLog, partner, partners.size());
            reportDao.save(report);
        }

        //Save the preaching log at the end to ensure that the report was generated correctly
        preachingLog.setUpdatedDate(new Date());
        return preachingLogDao.save(preachingLog);
    }

    @Override
    public void delete(Integer preachingLogId) throws NotFoundException {
        Optional<PreachingLog> preachingLogOptional = preachingLogDao.findById(preachingLogId);

        if (!preachingLogOptional.isPresent())
            throw new NotFoundException("Cannot delete a preaching log that does not exist");

        PreachingLog preachingLog = preachingLogOptional.get();

        //subtract from the associated reports
        List<Integer> partners = new ArrayList<>();
        if (preachingLog.getUserId1() != null) { partners.add(preachingLog.getUserId1()); }
        if (preachingLog.getUserId2() != null) { partners.add(preachingLog.getUserId2()); }
        if (preachingLog.getUserId3() != null) { partners.add(preachingLog.getUserId3()); }

        for (Integer partnerId : partners) {
            deleteFromUserReport(preachingLog, partnerId, partners.size());
        }

        preachingLogDao.delete(preachingLog);
    }

    private Report createOrUpdateUserReport(PreachingLog preachingLog, Partner partner, int partnerCount) {

        Report previousReport = reportDao.getReportByUserIdAndDate(partner.getId(), preachingLog.getDate());

        // the user does not have a report for this date yet, need to create it
        if (previousReport == null) previousReport = new Report();

        //if a previous preaching log exists already then this must be an update. we must first decrement the old preaching log info, and increment by the new preaching log info
        Optional<PreachingLog> previousPreachingLogOptional = preachingLogDao.findById(preachingLog.getId());
        if (previousPreachingLogOptional.isPresent()) {
            PreachingLog previousPreachingLog = previousPreachingLogOptional.get();
            Report decrementedReport = decrementReport(previousReport, previousPreachingLog, partnerCount);
            return incrementReport(decrementedReport, preachingLog, partner, partnerCount);
        } else {
            // no previous preaching log so we are just saving the increment
            return incrementReport(previousReport, preachingLog, partner, partnerCount);
        }

    }

    private void deleteFromUserReport(PreachingLog preachingLog, Integer partnerId, int partnerCount) {
        Report report = reportDao.getReportByUserIdAndDate(partnerId, preachingLog.getDate());

        if (report != null) {
            Report modifiedReport = decrementReport(report, preachingLog, partnerCount);
            reportDao.save(modifiedReport);
        }
    }

    private Report decrementReport(Report previousReport, PreachingLog previousPreachingLog, int partnerCount) {
        Report returnedReport = new Report(previousReport);
        returnedReport.setPreachedSession(previousReport.getPreachedSession() - previousPreachingLog.getPreached());
        returnedReport.setContactsSession(previousReport.getContactsSession() - previousPreachingLog.getContacts());
        returnedReport.setBaptismsSession(previousReport.getBaptismsSession() - previousPreachingLog.getBaptisms());
        returnedReport.setFruitsSession(previousReport.getFruitsSession() - previousPreachingLog.getFruits());
        returnedReport.setTalentsSession(previousReport.getTalentsSession() - previousPreachingLog.getTalents());
        returnedReport.setPreachedIndividual(previousReport.getPreachedIndividual() - ((double)previousPreachingLog.getPreached() / partnerCount));
        returnedReport.setContactsIndividual(previousReport.getContactsIndividual() - ((double)previousPreachingLog.getContacts() / partnerCount));
        returnedReport.setBaptismsIndividual(previousReport.getBaptismsIndividual() - ((double)previousPreachingLog.getBaptisms() / partnerCount));
        returnedReport.setFruitsIndividual(previousReport.getFruitsIndividual() - previousPreachingLog.getFruits());
        returnedReport.setTalentsIndividual(previousReport.getTalentsIndividual() - previousPreachingLog.getTalents());
        returnedReport.setUpdatedDate(new Date());

        return returnedReport;
    }

    private Report incrementReport(Report previousReport, PreachingLog newPreachingLog, Partner partner, int partnerCount) {
        Report returnedReport = new Report();

        previousReport.setUserId(partner.getId());
        previousReport.setTeamId(partner.getTeamId());
        previousReport.setGroupId(partner.getGroupId());
        previousReport.setChurchId(partner.getChurchId());
        previousReport.setMovementId(newPreachingLog.getMovementId());
        previousReport.setDate(newPreachingLog.getDate());
        previousReport.setPreachedSession(previousReport.getPreachedSession() + newPreachingLog.getPreached());
        previousReport.setContactsSession(previousReport.getContactsSession() + newPreachingLog.getContacts());
        previousReport.setBaptismsSession(previousReport.getBaptismsSession() + newPreachingLog.getBaptisms());
        previousReport.setFruitsSession(previousReport.getFruitsSession() + newPreachingLog.getFruits());
        previousReport.setTalentsSession(previousReport.getTalentsSession() + newPreachingLog.getTalents());
        previousReport.setPreachedIndividual(previousReport.getPreachedIndividual() + ((double) newPreachingLog.getPreached() / partnerCount));
        previousReport.setContactsIndividual(previousReport.getContactsIndividual() + ((double) newPreachingLog.getContacts() / partnerCount));
        previousReport.setBaptismsIndividual(previousReport.getBaptismsIndividual() + ((double) newPreachingLog.getBaptisms() / partnerCount));
        previousReport.setFruitsIndividual(previousReport.getFruitsIndividual() + newPreachingLog.getFruits());
        previousReport.setTalentsIndividual(previousReport.getTalentsIndividual() + newPreachingLog.getTalents());
        previousReport.setUpdatedDate(new Date());

        return returnedReport;
    }
}
