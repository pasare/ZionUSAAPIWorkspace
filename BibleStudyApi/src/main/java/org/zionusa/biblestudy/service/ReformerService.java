package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.biblestudy.dao.ReformerDao;
import org.zionusa.biblestudy.dao.ReformerTypeDao;
import org.zionusa.biblestudy.domain.*;
import org.zionusa.biblestudy.util.Utilities;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReformerService extends BaseService<Reformer, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ReformerService.class);
    private final ReformerDao reformerDao;
    private final ReformerTypeDao reformerTypeDao;

    @Autowired
    public ReformerService(ReformerDao reformerDao, ReformerTypeDao reformerTypeDao) {
        super(reformerDao, logger, Reformer.class);
        this.reformerDao = reformerDao;
        this.reformerTypeDao = reformerTypeDao;
    }

    public List<Reformer> getByUser(Integer userId) {
        return reformerDao.findAllByUserId(userId);
    }

    public List<Reformer> getByChurch(Integer churchId) {
        return reformerDao.findAllByChurchId(churchId);
    }

    public List<Reformer> getByParentChurch(Integer parentChurchId) {
        return reformerDao.findAllByParentChurchId(parentChurchId);
    }

    public Reformer save(Reformer reformer) {
        //  Get the reformer report from the previous week
        LocalDate currentStartDate = LocalDate.parse(reformer.getStartDate());
        LocalDate lastWeekStartDate = currentStartDate.minus(1, ChronoUnit.WEEKS);
        Reformer lastWeekReformer = reformerDao.findByUserIdAndStartDate(reformer.getUserId(),
            lastWeekStartDate.atStartOfDay().format(DateTimeFormatter.ISO_DATE));

        // this is a new entry for this week, try and pull last weeks entry and bring the unchanging fields over
        if (reformer.getId() == null || reformer.getId() == 0) {
            logger.warn("Creating a new reformer report for {} (UserId: {}) for {} to {}", reformer.getName(), reformer.getUserId(), reformer.getStartDate(), reformer.getEndDate());

            // User did not submit a report last week, get the users last report and copy the values from there
            if (lastWeekReformer == null) {
                List<Reformer> lastResults = reformerDao.findAllByUserId(reformer.getUserId());

                if (!lastResults.isEmpty()) {
                    lastResults.sort(Utilities.reformerComparatorDesc);
                    lastWeekReformer = lastResults.get(0);
                }
            }

            // we got a result from last week, now update
            if (lastWeekReformer != null) {
                reformer.setFullAttendance(lastWeekReformer.getFullAttendance());
                reformer.setContactHasFiveBibleStudies(lastWeekReformer.getContactHasFiveBibleStudies());
                reformer.setCurrentReformerTypeId(lastWeekReformer.getFutureReformerTypeId());
                reformer.setFutureReformerTypeId(lastWeekReformer.getFutureReformerTypeId());
                reformer.setFruitBecomesVolunteer(lastWeekReformer.getFruitBecomesVolunteer());
                reformer.setLevelOneSermonBook(lastWeekReformer.getLevelOneSermonBook());
                reformer.setLreCenturiesTest(lastWeekReformer.getLreCenturiesTest());
                reformer.setLreTimelineTest(lastWeekReformer.getLreTimelineTest());
                reformer.setLreTeacher(lastWeekReformer.getLreTeacher());
                reformer.setLreTimelineBrochure(lastWeekReformer.getLreTimelineBrochure());
                reformer.setPatriotPledgeSigned(lastWeekReformer.getPatriotPledgeSigned());
                reformer.setBearFruit(lastWeekReformer.getBearFruit());
                reformer.setTithing(lastWeekReformer.getTithing());
            } else {
                logger.warn("A report from the previous weeks was not found for this member setting defaults");
                reformer.setCurrentReformerTypeId(1);
                reformer.setFutureReformerTypeId(1);
            }

            return reformerDao.save(reformer);
        }

        // Get the reformer type id from last week to determine if they can rank up this week
        Integer lastWeekCurrentReformerTypeId = null;
        if (lastWeekReformer != null) {
            lastWeekCurrentReformerTypeId = lastWeekReformer.getCurrentReformerTypeId();
        }

        //determine if they meet the qualifications to rank up
        return determineRank(reformer, lastWeekCurrentReformerTypeId);
    }

    public ReformerResult getLatestReformerResult(Integer reformerId) {
        return null;
    }

    public ReformerResult getReformerResultByDate(Integer userId, String startDate, String endDate) {
        reformerDao.findAllByUserIdAndStartDateBetween(userId, startDate, endDate);
        return null;
    }

    public ReformerReport getEastCoastReformerReport(String startDate, String endDate) {
        List<Reformer> reformers = reformerDao.findAllByStartDateBetween(startDate, endDate);
        ReformerReport reformerResult = new ReformerReport(0, "East Coast", startDate, endDate);

        return generateReformerReport(reformers, reformerResult);
    }

    public ReformerReport getChurchReformerReport(Integer churchId, String startDate, String endDate) {
        List<Reformer> reformers = reformerDao.findAllByChurchIdAndStartDateBetween(churchId, startDate, endDate);
        if (!reformers.isEmpty()) {
            ReformerReport churchResult = new ReformerReport(churchId, reformers.get(0).getChurchName(), startDate, endDate);
            return generateReformerReport(reformers, churchResult);
        }


        return new ReformerReport();
    }

    public ReformerReport getGroupReformerReport(Integer groupId, String startDate, String endDate) {
        List<Reformer> reformers = reformerDao.findAllByGroupIdAndStartDateBetween(groupId, startDate, endDate);
        if (!reformers.isEmpty()) {
            ReformerReport groupResult = new ReformerReport(groupId, reformers.get(0).getChurchName(), startDate, endDate);
            return generateReformerReport(reformers, groupResult);
        }


        return new ReformerReport();
    }

    public ReformerReport getTeamReformerReport(Integer teamId, String startDate, String endDate) {
        List<Reformer> reformers = reformerDao.findAllByTeamIdAndStartDateBetween(teamId, startDate, endDate);
        if (!reformers.isEmpty()) {
            ReformerReport churchResult = new ReformerReport(teamId, reformers.get(0).getChurchName(), startDate, endDate);
            return generateReformerReport(reformers, churchResult);
        }


        return new ReformerReport();
    }

    private ReformerReport generateReformerReport(List<Reformer> reformerList, ReformerReport reformerReport) {
        int totalPreached = 0;
        int totalStudies = 0;
        int totalFruits = 0;
        int volunteers = 0;
        int patriots = 0;
        int activists = 0;
        int reformers = 0;
        int lastReformers = 0;
        int awardWinners = 0;

        List<Integer> seenReformers = new ArrayList<>();

        // generate an east coast report adding all the numbers together
        for (Reformer reformer : reformerList) {
            totalPreached += reformer.getWeeklyPreachingCount() != null ? reformer.getWeeklyPreachingCount() : 0;
            totalStudies += reformer.getWeeklyBibleStudies() != null ? reformer.getWeeklyBibleStudies() : 0;
            totalFruits += reformer.getWeeklyFruitCount() != null ? reformer.getWeeklyFruitCount() : 0;

            if (!seenReformers.contains(reformer.getUserId()) && reformer.getCurrentReformerType() != null) {
                switch (reformer.getCurrentReformerType().getSingularName()) {
                    case "Volunteer":
                        volunteers += 1;
                        break;
                    case "Patriot":
                        patriots += 1;
                        break;
                    case "Activist":
                        activists += 1;
                        break;
                    case "Reformer":
                        reformers += 1;
                        break;
                    case "Last Reformer":
                        lastReformers += 1;
                        break;
                    case "Award Winner":
                        awardWinners += 1;
                }

                //record this reformer as already seen so we do not count them twice
                seenReformers.add(reformer.getUserId());
            }
        }

        reformerReport.setTotalPreached(totalPreached);
        reformerReport.setTotalStudies(totalStudies);
        reformerReport.setTotalFruits(totalFruits);
        reformerReport.setVolunteers(volunteers);
        reformerReport.setActivists(activists);
        reformerReport.setPatriots(patriots);
        reformerReport.setReformers(reformers);
        reformerReport.setLastReformers(lastReformers);
        reformerReport.setAwardWinners(awardWinners);
        reformerReport.setReformerList(reformerList);

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        reformerReport.setLastGeneratedTime(timestamp.toInstant().toEpochMilli());

        return reformerReport;
    }

    private Reformer determineRank(Reformer reformer, Integer lastWeekCurrentReformerTypeId) {
        // To advance, they must have a report from this week with reformer type id
        if (reformer.getCurrentReformerTypeId() != null) {
            Optional<ReformerType> currentReformerType = reformerTypeDao.findById(reformer.getCurrentReformerTypeId());

            String lastReformerTypeSingularName = "Non-Participant";
            if (lastWeekCurrentReformerTypeId != null) {
                Optional<ReformerType> lastReformerType = reformerTypeDao.findById(lastWeekCurrentReformerTypeId);

                lastReformerTypeSingularName = lastReformerType.isPresent() ? lastReformerType.get().getSingularName() : "Volunteer";
            }
            logger.warn("Last week, {} (UserId: {}) was a {}", reformer.getName(), reformer.getUserId(), lastReformerTypeSingularName);

            if (currentReformerType.isPresent()) {
                // based on their current level determine what the next level should be
                ArrayList<String> reasonsToNotAdvance;
                String currentReformerTypeString = currentReformerType.get().getSingularName();
                String message;
                String NO_COMPLETED_TASKS = "Did not complete any tasks this week.";
                switch (currentReformerTypeString) {
                    case "Volunteer":
                        logger.warn("Checking if {} can advance to Patriot", reformer.getName());

                        // Check if there are any reasons this user should not advance to patriot
                        reasonsToNotAdvance = determinePatriotRankUp(reformer);

                        if (lastWeekCurrentReformerTypeId != null && lastWeekCurrentReformerTypeId.equals(reformer.getCurrentReformerTypeId()) && reasonsToNotAdvance.size() == 0) {
                            message = FulfilledWithTwoWeekRequirement(currentReformerTypeString, "Patriot");
                            reformer.setFutureReformerTypeId(2);
                            reformer.setStatus(ReformerStatus.Advancing.name());
                            reformer.setAdvancementReasons("");
                            logger.warn("{}, {}", reformer.getName(), message);
                        } else {
                            // The user will not advance
                            reformer.setFutureReformerTypeId(1);

                            int PATRIOT_REQUIREMENT_COUNT = 5;
                            if (reasonsToNotAdvance.size() == 0) {
                                message = FulfilledWithoutTwoWeekRequirement(currentReformerTypeString);
                                reformer.setStatus(ReformerStatus.Fulfilled.name());
                            } else if (reasonsToNotAdvance.size() < PATRIOT_REQUIREMENT_COUNT) {
                                message = flattenArrayListString(reasonsToNotAdvance);
                                reformer.setStatus(ReformerStatus.MakingEffort.name());
                            } else {
                                message = NO_COMPLETED_TASKS;
                                reformer.setStatus(ReformerStatus.NoTaskCompleted.name());
                            }
                            reformer.setAdvancementReasons(message);
                            logger.warn("{}: {}", reformer.getName(), message);
                        }
                        break;
                    case "Patriot":
                        logger.warn("Checking if {} can advance to Activist", reformer.getName());

                        // Check if there are any reasons this user should not advance to activist
                        reasonsToNotAdvance = determineActivistRankUp(reformer);

                        if (lastWeekCurrentReformerTypeId != null && lastWeekCurrentReformerTypeId.equals(reformer.getCurrentReformerTypeId()) && reasonsToNotAdvance.size() == 0) {
                            message = FulfilledWithTwoWeekRequirement(currentReformerTypeString, "Activist");
                            reformer.setFutureReformerTypeId(3);
                            reformer.setStatus(ReformerStatus.Advancing.name());
                            reformer.setAdvancementReasons("");
                            logger.warn("{}, {}", reformer.getName(), message);
                        } else {
                            // The user will not advance
                            reformer.setFutureReformerTypeId(2);

                            int ACTIVIST_REQUIREMENT_COUNT = 6;
                            if (reasonsToNotAdvance.size() == 0) {
                                message = FulfilledWithoutTwoWeekRequirement(currentReformerTypeString);
                                reformer.setStatus(ReformerStatus.Fulfilled.name());
                            } else if (reasonsToNotAdvance.size() < ACTIVIST_REQUIREMENT_COUNT) {
                                message = flattenArrayListString(reasonsToNotAdvance);
                                reformer.setStatus(ReformerStatus.MakingEffort.name());
                            } else {
                                message = NO_COMPLETED_TASKS;
                                reformer.setStatus(ReformerStatus.NoTaskCompleted.name());
                            }
                            reformer.setAdvancementReasons(message);
                            logger.warn("{}: {}", reformer.getName(), message);
                        }
                        break;
                    case "Activist":
                        logger.warn("Checking if {} can advance to Reformer", reformer.getName());

                        // Check if there are any reasons this user should not advance to reformer
                        reasonsToNotAdvance = determineReformerRankUp(reformer);

                        if (lastWeekCurrentReformerTypeId != null && lastWeekCurrentReformerTypeId.equals(reformer.getCurrentReformerTypeId()) && reasonsToNotAdvance.size() == 0) {
                            message = FulfilledWithTwoWeekRequirement(currentReformerTypeString, "Reformer");
                            reformer.setFutureReformerTypeId(4);
                            reformer.setStatus(ReformerStatus.Advancing.name());
                            reformer.setAdvancementReasons("");
                            logger.warn("{}, {}", reformer.getName(), message);
                        } else {
                            // The user will not advance
                            reformer.setFutureReformerTypeId(3);

                            int REFORMER_REQUIREMENT_COUNT = 8;
                            if (reasonsToNotAdvance.size() == 0) {
                                message = FulfilledWithoutTwoWeekRequirement(currentReformerTypeString);
                                reformer.setStatus(ReformerStatus.Fulfilled.name());
                            } else if (reasonsToNotAdvance.size() < REFORMER_REQUIREMENT_COUNT) {
                                message = flattenArrayListString(reasonsToNotAdvance);
                                reformer.setStatus(ReformerStatus.MakingEffort.name());
                            } else {
                                message = NO_COMPLETED_TASKS;
                                reformer.setStatus(ReformerStatus.NoTaskCompleted.name());
                            }
                            reformer.setAdvancementReasons(message);
                            logger.warn("{}: {}", reformer.getName(), message);
                        }
                        break;
                    case "Reformer":
                        logger.warn("Checking if {} can advance to Last Reformer", reformer.getName());

                        // Check if there are any reasons this user should not advance to last reformer
                        reasonsToNotAdvance = determineLastReformerRankUp(reformer);

                        if (lastWeekCurrentReformerTypeId != null && lastWeekCurrentReformerTypeId.equals(reformer.getCurrentReformerTypeId()) && reasonsToNotAdvance.size() == 0) {
                            message = FulfilledWithTwoWeekRequirement(currentReformerTypeString, "Last Reformer");
                            reformer.setFutureReformerTypeId(5);
                            reformer.setStatus(ReformerStatus.Advancing.name());
                            reformer.setAdvancementReasons("");
                            logger.warn("{}, {}", reformer.getName(), message);
                        } else {
                            // The user will not advance
                            reformer.setFutureReformerTypeId(4);

                            int LAST_REFORMER_REQUIREMENT_COUNT = 10;
                            if (reasonsToNotAdvance.size() == 0) {
                                message = FulfilledWithoutTwoWeekRequirement(currentReformerTypeString);
                                reformer.setStatus(ReformerStatus.Fulfilled.name());
                            } else if (reasonsToNotAdvance.size() < LAST_REFORMER_REQUIREMENT_COUNT) {
                                message = flattenArrayListString(reasonsToNotAdvance);
                                reformer.setStatus(ReformerStatus.MakingEffort.name());
                            } else {
                                message = NO_COMPLETED_TASKS;
                                reformer.setStatus(ReformerStatus.NoTaskCompleted.name());
                            }
                            reformer.setAdvancementReasons(message);
                            logger.warn("{}: {}", reformer.getName(), message);
                        }
                        break;
                    case "Last Reformer":
                        logger.warn("Checking if {} can become an Award Winner", reformer.getName());

                        // Check if there are any reasons this user should not advance to award winner
                        reasonsToNotAdvance = determineAwardWinner(reformer);

                        if (lastWeekCurrentReformerTypeId != null && lastWeekCurrentReformerTypeId.equals(reformer.getCurrentReformerTypeId()) && reasonsToNotAdvance.size() == 0) {
                            message = FulfilledWithTwoWeekRequirement(currentReformerTypeString, "Award Winner");
                            reformer.setFutureReformerTypeId(6);
                            reformer.setStatus(ReformerStatus.Advancing.name());
                            reformer.setAdvancementReasons("");
                            logger.warn("{}, {}", reformer.getName(), message);
                        } else {
                            // The user will not advance
                            reformer.setFutureReformerTypeId(5);

                            int AWARD_WINNER_REQUIREMENT_COUNT = 12;
                            if (reasonsToNotAdvance.size() == 0) {
                                message = FulfilledWithoutTwoWeekRequirement(currentReformerTypeString);
                                reformer.setStatus(ReformerStatus.Fulfilled.name());
                            } else if (reasonsToNotAdvance.size() < AWARD_WINNER_REQUIREMENT_COUNT) {
                                message = flattenArrayListString(reasonsToNotAdvance);
                                reformer.setStatus(ReformerStatus.MakingEffort.name());
                            } else {
                                message = NO_COMPLETED_TASKS;
                                reformer.setStatus(ReformerStatus.NoTaskCompleted.name());
                            }
                            reformer.setAdvancementReasons(message);
                            logger.warn("{}: {}", reformer.getName(), message);
                        }
                        break;
                    case "Award Winner":
                        logger.warn("{}: Award winner! don't stop, don't give up!", reformer.getName());
                        break;
                    default:
                        logger.error("{}: Reformer type not found  {}", reformer.getName(), currentReformerTypeString);
                        break;
                }
            } else {
                reformer.setCurrentReformerTypeId(reformer.getCurrentReformerTypeId());
                logger.error("Could not find current reformer type");
            }
        } else {
            logger.warn("Setting defaults for current and future reformer type to volunteer");
            reformer.setCurrentReformerTypeId(1);
            reformer.setFutureReformerTypeId(1);
        }

        return reformerDao.save(reformer);
    }

    private ArrayList<String> determinePatriotRankUp(Reformer reformer) {
        ArrayList<String> rankUp = new ArrayList<>();

        if (reformer.getFullAttendance() == null || !reformer.getFullAttendance()) {
            rankUp.add("Did not keep full attendance yet.");
        }
        if (reformer.getTithing() == null || !reformer.getTithing()) {
            rankUp.add("Did not keep the order of Melchizedek yet.");
        }
        if (reformer.getLevelOneSermonBook() == null || !reformer.getLevelOneSermonBook()) {
            rankUp.add("Did not have Sermon Book Level 1 yet.");
        }
        if (reformer.getPatriotPledgeSigned() == null || !reformer.getPatriotPledgeSigned()) {
            rankUp.add("Did not sign the patriot pledge yet.");
        }
        if (reformer.getWeeklyPreachingCount() < 21) {
            rankUp.add("Did not preach to at least 21 people this week.");
        }

        return rankUp;
    }

    private ArrayList<String> determineActivistRankUp(Reformer reformer) {
        ArrayList<String> rankUp = determinePatriotRankUp(reformer);

        if (reformer.getLreTimelineTest() == null || !reformer.getLreTimelineTest()) {
            rankUp.add("Did not pass the timeline test yet.");
        }

        return rankUp;
    }

    private ArrayList<String> determineReformerRankUp(Reformer reformer) {
        ArrayList<String> rankUp = determineActivistRankUp(reformer);

        if (reformer.getLreTimelineBrochure() == null || !reformer.getLreTimelineBrochure()) {
            rankUp.add("Did not preach with the LRE timeline brochure yet.");
        }
        if (reformer.getContactHasFiveBibleStudies() == null || !reformer.getContactHasFiveBibleStudies()) {
            rankUp.add("Did not have a contact who studied five or more bible studies yet.");
        }

        return rankUp;
    }

    private ArrayList<String> determineLastReformerRankUp(Reformer reformer) {
        ArrayList<String> rankUp = determineReformerRankUp(reformer);

        if (reformer.getLreCenturiesTest() == null || !reformer.getLreCenturiesTest()) {
            rankUp.add("Did not pass the centuries test yet.");
        }
        if (reformer.getBearFruit() == null || !reformer.getBearFruit()) {
            rankUp.add("Did not bear fruit yet.");
        }

        return rankUp;
    }

    private ArrayList<String> determineAwardWinner(Reformer reformer) {
        ArrayList<String> rankUp = determineLastReformerRankUp(reformer);

        if (reformer.getFruitBecomesVolunteer() == null || !reformer.getFruitBecomesVolunteer()) {
            rankUp.add("Did not have a fruit become a volunteer yet.");
        }
        if (reformer.getLreTeacher() == null || !reformer.getLreTeacher()) {
            rankUp.add("Did not have become a LRE Certified Guide yet.");
        }

        return rankUp;
    }

    private String flattenArrayListString(ArrayList<String> list) {
        String stringList = list.toString();
        return stringList.substring(1, stringList.length() - 1);
    }

    private String FulfilledWithTwoWeekRequirement(String rank, String nextRank) {
        return "Fulfilled the requirements while a " + rank.toLowerCase() + " for two weeks and will advance to " + nextRank.toLowerCase() + "!";
    }

    private String FulfilledWithoutTwoWeekRequirement(String rank) {
        return "Fulfilled the requirements but need to be a " + rank.toLowerCase() + " for two weeks.";
    }
}
