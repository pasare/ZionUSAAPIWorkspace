package org.zionusa.event.domain.resultsSurvey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.domain.UserPermission;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.Util;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.event.authorizer.PermissionsAuthorizer;
import org.zionusa.event.dao.EventProposalTableViewDao;
import org.zionusa.event.domain.eventProposal.EventProposal;
import org.zionusa.event.domain.eventProposal.EventProposalsDao;
import org.zionusa.event.domain.EventProposalTableView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.zionusa.base.util.Util.coalesce;
import static org.zionusa.base.util.Util.getDateDiff;


@Service
public class ResultsSurveysService extends BaseService<ResultsSurvey, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ResultsSurveysService.class);
    private static final String MAN_HOURS_COLUMN = "manHours";
    private static final String MAN_HOURS_PER_VOLUNTEER_COLUMN = "manHoursPerVolunteer";
    private static final String BENEFICIARIES_AGE_RANGE = "beneficiariesAgeRange";

    public final EventProposalsDao eventProposalsDao;
    public final EventProposalTableViewDao eventProposalTableViewDao;
    public final PermissionsAuthorizer permissionsAuthorizer;
    public final ResultsSurveysDao resultsSurveysDao;
    public final ResultsSurveyViewDao resultsSurveyViewDao;

    @Autowired
    public ResultsSurveysService(EventProposalsDao eventProposalsDao,
                                 EventProposalTableViewDao eventProposalTableViewDao,
                                 PermissionsAuthorizer permissionsAuthorizer,
                                 ResultsSurveysDao resultsSurveysDao,
                                 ResultsSurveyViewDao resultsSurveyViewDao) {
        super(resultsSurveysDao, logger, ResultsSurvey.class);
        this.eventProposalsDao = eventProposalsDao;
        this.eventProposalTableViewDao = eventProposalTableViewDao;
        this.permissionsAuthorizer = permissionsAuthorizer;
        this.resultsSurveysDao = resultsSurveysDao;
        this.resultsSurveyViewDao = resultsSurveyViewDao;
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_RESULTS_SURVEY + "')")
    public List<ResultsSurvey> getAll(Boolean archived) {
        return super.getAll(archived);
    }



    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_RESULTS_SURVEY + "')")
    public Page<ResultsSurvey> getAllByPage(Pageable pageable) {
        return super.getAllByPage(pageable);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_RESULTS_SURVEY + "')")
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        final AuthenticatedUser authenticatedUser = permissionsAuthorizer.getAuthenticatedUser();
        final List<ResultsSurveyView> items = permissionsAuthorizer.hasEventAdminReadAccess()
            // Admin views all results surveys
            ? resultsSurveyViewDao.getAllByArchive(Boolean.TRUE.equals(archived))
            // Only view in your branch
            : resultsSurveyViewDao.getAllByArchiveAndBranchId(Boolean.TRUE.equals(archived), authenticatedUser.getChurchId());

        final List<Map<String, Object>> displayItems = new ArrayList<>();

        items.forEach(item -> {
            Map<String, Object> displayItem = Util.getFieldsAndValues(columns, item);

            // Man hours calculation
            if (columns.contains(MAN_HOURS_COLUMN)) {
                displayItem.put(MAN_HOURS_COLUMN, getVolunteerManHoursPerVolunteer(item) * getTotalVolunteers(item));
            }

            // Man hours per volunteer calculation
            if (columns.contains(MAN_HOURS_PER_VOLUNTEER_COLUMN)) {
                displayItem.put(MAN_HOURS_PER_VOLUNTEER_COLUMN, getVolunteerManHoursPerVolunteer(item));
            }

            if (displayItem.size() > 0) displayItems.add(displayItem);
        });


        return displayItems;
    }

    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.VIEW_RESULTS_SURVEY + "')")
    public ResultsSurveyView getDisplayById(Integer id) throws NotFoundException {
        Optional<ResultsSurveyView> resultsSurveyViewOptional = resultsSurveyViewDao.findById(id);

        if (resultsSurveyViewOptional.isPresent()) {
            ResultsSurveyView resultsSurveyView = resultsSurveyViewOptional.get();

            if (resultsSurveyView.getEventProposalId() != null) {
                Optional<EventProposalTableView> eventProposalTableViewOptional = eventProposalTableViewDao.findById(resultsSurveyView.getEventProposalId());
                eventProposalTableViewOptional.ifPresent(resultsSurveyView::setEventProposal);
            }

            final Integer manHoursPerVolunteer = getVolunteerManHoursPerVolunteer(resultsSurveyView);
            resultsSurveyView.setManHoursPerVolunteerCalculated(manHoursPerVolunteer);
            resultsSurveyView.setManHours(manHoursPerVolunteer * getTotalVolunteers(resultsSurveyView));

            return resultsSurveyView;
        }

        throw new NotFoundException();
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_RESULTS_SURVEY + "')")
    public ResultsSurvey patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }


    @Override
    @Transactional
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_RESULTS_SURVEY + "')")
    public ResultsSurvey save(ResultsSurvey resultsSurvey) {
        BigDecimal bg1,bg2,bg3,bg4;
        MathContext mc = new MathContext(2);

        bg1 = resultsSurvey.getDonation1Amount().add(resultsSurvey.getDonation2Amount());
        bg2 = resultsSurvey.getDonation3Amount().add(resultsSurvey.getDonation4Amount());
        bg3 = resultsSurvey.getDonation5Amount().add(resultsSurvey.getDonation6Amount());
        bg4 = bg1.add(bg2,mc);
        resultsSurvey.setTotalDonationAmount( bg3.add(bg4, mc));

        resultsSurvey.setDistanceCleanedInKm(resultsSurvey.getDistanceCleanedInMiles() * 1.609344);

        resultsSurvey.setBeneficiariesAgeRange(resultsSurvey.getMinBeneficiariesAgeRange()
            + " - " + resultsSurvey.getMaxBeneficiariesAgeRange());
        return super.save (resultsSurvey);
    };

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.EDIT_RESULTS_SURVEY + "')")
    public List<ResultsSurvey> saveMultiple(List<ResultsSurvey> tList) {
        return super.saveMultiple(tList);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.DELETE_RESULTS_SURVEY + "')")
    public void delete(Integer id) throws NotFoundException {
        super.delete(id);
    }

    @Override
    @PreAuthorize("@permissionsAuthorizer.hasPermission('" + UserPermission.DELETE_RESULTS_SURVEY + "')")
    public void deleteMultiple(String ids) throws NotFoundException {
        super.deleteMultiple(ids);
    }

    private Integer getVolunteerManHoursPerVolunteer(ResultsSurveyView item) {
        if (item.getManHoursPerVolunteer() != null) {
            // Calculate based on man hours per volunteer
            return item.getManHoursPerVolunteer();
        } else {
            // Calculate based on the length of the event
            try {
                final Date proposedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(
                    item.getProposedDate() != null ? item.getProposedDate() : item.getEventProposalProposedDate());
                final Date proposedEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(
                    item.getProposedEndDate() != null ? item.getProposedEndDate() : item.getEventProposalProposedEndDate());
                final int dayDiff = (int) getDateDiff(proposedDate, proposedEndDate, TimeUnit.HOURS);
                final Date proposedTime = new SimpleDateFormat("h:mm a", Locale.ENGLISH).parse(
                    item.getProposedTime() != null ? item.getProposedTime() : item.getEventProposalProposedTime());
                final Date proposedEndTime = new SimpleDateFormat("h:mm a", Locale.ENGLISH).parse(
                    item.getProposedEndTime() != null ? item.getProposedEndTime() : item.getEventProposalProposedEndTime());
                final int hourDiff = (int) getDateDiff(proposedTime, proposedEndTime, TimeUnit.HOURS);
                return Math.abs(dayDiff + hourDiff);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }

        return 0;
    }

    private Integer getTotalVolunteers(ResultsSurveyView item) {
        return coalesce(item.getVolunteerFemaleAdults()) +
            coalesce(item.getVolunteerMaleAdults()) + coalesce(item.getVolunteerFemaleCollegeStudents()) +
            coalesce(item.getVolunteerMaleCollegeStudents()) + coalesce(item.getVolunteerFemaleTeenagers()) +
            coalesce(item.getVolunteerMaleTeenagers()) + coalesce(item.getVolunteerFemaleYoungAdults()) +
            coalesce(item.getVolunteerMaleYoungAdults()) + coalesce(item.getNonMembers());
    }
}
