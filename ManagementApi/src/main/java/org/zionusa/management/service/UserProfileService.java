package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.zionusa.base.enums.EZoneId;
import org.zionusa.base.util.Util;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.management.dao.UserProfileCategoriesDao;
import org.zionusa.management.dao.UserProfileDao;
import org.zionusa.management.dao.UserProfileInformationDao;
import org.zionusa.management.dao.UserProfileUserDao;
import org.zionusa.management.domain.User;
import org.zionusa.management.domain.UserProfileCategory;
import org.zionusa.management.domain.UserProfileInformation;
import org.zionusa.management.domain.userprofile.UserProfile;
import org.zionusa.management.domain.userprofile.UserProfileSearchView;
import org.zionusa.management.domain.userprofile.UserProfileSearchViewDao;
import org.zionusa.management.exception.NotFoundException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "users-cache")
public class UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);
    private final UserProfileDao userProfileDao;
    private final UserProfileCategoriesDao userProfileCategoriesDao;
    private final UserProfileInformationDao userProfileInformationDao;
    private final UserProfileUserDao userProfileUserDao;

    private final UserProfileSearchViewDao userProfileSearchViewDao;

    @Autowired
    public UserProfileService(UserProfileDao userProfileDao,
                              UserProfileCategoriesDao userProfileCategoriesDao,
                              UserProfileInformationDao userProfileInformationDao,
                              UserProfileUserDao userProfileUserDao,
                              UserProfileSearchViewDao userProfileSearchViewDao) {
        this.userProfileDao = userProfileDao;
        this.userProfileCategoriesDao = userProfileCategoriesDao;
        this.userProfileInformationDao = userProfileInformationDao;
        this.userProfileUserDao = userProfileUserDao;
        this.userProfileSearchViewDao = userProfileSearchViewDao;
    }

    public List<UserProfileSearchView> getAll() {
        log.info("getting profiles for all users ({})", LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        return userProfileSearchViewDao.findAll();
    }

    public List<UserProfileSearchView> findProfileUsersByBranchId(Integer branchId) {
        log.info("getting profiles for users from branch {} ({})", branchId, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        return userProfileSearchViewDao.findAllByBranchIdOrMainBranchIdOrParentBranchId(branchId, branchId, branchId);
    }

    public List<UserProfileSearchView> findUserProfilesByGroupId(Integer groupId) {
        log.info("getting profiles for users from group {} ({})", groupId, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        return userProfileSearchViewDao.findAllByGroupId(groupId);
    }

    public List<UserProfileSearchView> findUserProfilesByTeamId(Integer teamId) {
        log.info("getting profiles for users from team {} ({})", teamId, LocalDateTime.now(EZoneId.NEW_YORK.getValue()));
        return userProfileSearchViewDao.findAllByTeamId(teamId);
    }

    public List<Map<String, Object>> getAllDisplay(List<String> columns,
                                                   Integer associationId,
                                                   Integer churchId,
                                                   Integer groupId,
                                                   Integer teamId) {
        if (associationId != null) {
            log.info("Retrieving list of display items by associationId {}", associationId);
            return getAllDisplayFromList(userProfileUserDao.findProfileUsersByAssociationId(associationId), columns);
        }
        if (churchId != null) {
            log.info("Retrieving list of display items by churchId {}", churchId);
            return getAllDisplayFromList(userProfileUserDao.findProfileUsersByChurchId(churchId), columns);
        }
        if (groupId != null) {
            log.info("Retrieving list of display items by groupId {}", groupId);
            return getAllDisplayFromList(userProfileUserDao.findUserProfilesByGroupId(groupId), columns);
        }
        if (teamId != null) {
            log.info("Retrieving list of display items by teamId {}", teamId);
            return getAllDisplayFromList(userProfileUserDao.findUserProfilesByTeamId(teamId), columns);
        }
        // If admin
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authenticatedUser.getAccess().equals("Admin")) {
            return getAllDisplayFromList(userProfileUserDao.findAll(), columns);
        }
        return new ArrayList<>();
    }

    public List<Map<String, Object>> getAllDisplayFromList(List<User.ProfileUser> items, List<String> columns) {
        List<Map<String, Object>> displayItems = new ArrayList<>();

        items.forEach(item -> {
            Map<String, Object> displayContact = Util.getFieldsAndValues(columns, item);
            if (!displayContact.isEmpty()) displayItems.add(displayContact);
        });

        return displayItems;
    }

    public UserProfile getUserProfileByUserId(Integer userId) {
        return userProfileDao.findUserProfileByUserId(userId);
    }

    @Transactional
    public User.ProfileUser getByUserId(Integer userId) {
        User.ProfileUser profileUser = userProfileUserDao.findProfileUserById(userId);

        if (profileUser == null) {
            throw new NotFoundException("User profile not found");
        }

        UserProfile profile = userProfileDao.findUserProfileByUserId(userId);

        if (profile == null) {
            log.info("Creating a new profile");
            profile = new UserProfile();
            profile.setUserId(userId);
            profile = save(profile);
        }

        List<UserProfileInformation> userProfileInformationList = userProfileInformationDao.getUserProfileInformationByProfileId(profile.getId());
        List<UserProfileInformation> churchActivities = userProfileInformationList.stream().filter(x -> x.getCategoryId() == 1).collect(Collectors.toList());
        List<UserProfileInformation> churchPositions = userProfileInformationList.stream().filter(x -> x.getCategoryId() == 2).collect(Collectors.toList());
        List<UserProfileInformation> serviceDuties = userProfileInformationList.stream().filter(x -> x.getCategoryId() == 3).collect(Collectors.toList());
        List<UserProfileInformation> professionalSkills = userProfileInformationList.stream().filter(x -> x.getCategoryId() == 4).collect(Collectors.toList());
        List<UserProfileInformation> technicalSkills = userProfileInformationList.stream().filter(x -> x.getCategoryId() == 5).collect(Collectors.toList());
        List<UserProfileInformation> talents = userProfileInformationList.stream().filter(x -> x.getCategoryId() == 6).collect(Collectors.toList());
        // 7 Tithe
        // 8 Incident Report
        // 9 ?
        List<UserProfileInformation> previousChurches = userProfileInformationList.stream().filter(x -> x.getCategoryId() == 10).collect(Collectors.toList());
        List<UserProfileInformation> languages = userProfileInformationList.stream().filter(x -> x.getCategoryId() == 11).collect(Collectors.toList());
        List<UserProfileInformation> shortTermPreaching = userProfileInformationList.stream().filter(x -> x.getCategoryId() == 12).collect(Collectors.toList());

        profile.setChurchActivities(churchActivities);
        profile.setChurchPositions(churchPositions);
        profile.setServiceDuties(serviceDuties);
        profile.setProfessionalSkills(professionalSkills);
        profile.setTechnicalSkills(technicalSkills);
        profile.setTalents(talents);
        profile.setPreviousChurches(previousChurches);
        profile.setLanguages(languages);
        profile.setShortTermPreaching(shortTermPreaching);

        profileUser.setProfile(profile);

        return profileUser;
    }

    public UserProfile patchById(Integer id, Map<String, Object> fields) {
        log.info("Patching one with id of: {}", id);

        UserProfile item = getUserProfileByUserId(id);

        fields.forEach((key, value) -> {
            if (key.equals("id")) {
                log.info("- Cannot patch the id column");
            } else {
                // Use reflection to get field k on manager and set it to value v
                Field field = ReflectionUtils.findField(UserProfile.class, key);
                if (field == null) {
                    log.warn("- Field '{}' was not found", key);
                } else {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, item, value);
                }
            }
        });

        return userProfileDao.save(item);
    }

    @CacheEvict(cacheNames = "users-cache", allEntries = true)
    public UserProfile save(UserProfile profile) {

        UserProfile returnedProfile = userProfileDao.save(profile);

        // save all the sub categories
        List<UserProfileInformation> userProfileInformationList = new ArrayList<>();

        if (profile.getChurchActivities() != null) {
            profile.getChurchActivities().forEach(pi -> pi.setProfileId(returnedProfile.getId()));
            userProfileInformationList.addAll(profile.getChurchActivities());
        }
        if (profile.getChurchPositions() != null) {
            profile.getChurchPositions().forEach(pi -> pi.setProfileId(returnedProfile.getId()));
            userProfileInformationList.addAll(profile.getChurchPositions());
        }
        if (profile.getServiceDuties() != null) {
            profile.getServiceDuties().forEach(pi -> pi.setProfileId(returnedProfile.getId()));
            userProfileInformationList.addAll(profile.getServiceDuties());
        }
        if (profile.getProfessionalSkills() != null) {
            profile.getProfessionalSkills().forEach(pi -> pi.setProfileId(returnedProfile.getId()));
            userProfileInformationList.addAll(profile.getProfessionalSkills());
        }
        if (profile.getTechnicalSkills() != null) {
            profile.getTechnicalSkills().forEach(pi -> pi.setProfileId(returnedProfile.getId()));
            userProfileInformationList.addAll(profile.getTechnicalSkills());
        }
        if (profile.getTalents() != null) {
            profile.getTalents().forEach(pi -> pi.setProfileId(returnedProfile.getId()));
            userProfileInformationList.addAll(profile.getTalents());
        }
        if (profile.getPreviousChurches() != null) {
            profile.getPreviousChurches().forEach(pi -> pi.setProfileId(returnedProfile.getId()));
            userProfileInformationList.addAll(profile.getPreviousChurches());
        }
        if (profile.getLanguages() != null) {
            profile.getLanguages().forEach(pi -> pi.setProfileId(returnedProfile.getId()));
            userProfileInformationList.addAll(profile.getLanguages());
        }
        if (profile.getShortTermPreaching() != null) {
            profile.getShortTermPreaching().forEach(pi -> pi.setProfileId(returnedProfile.getId()));
            userProfileInformationList.addAll(profile.getShortTermPreaching());
        }

        // Refresh all profile information

        if (profile.getId() != null) {
            List<Integer> exemptCategories = new ArrayList<>();
            exemptCategories.add(7); // Tithe
            exemptCategories.add(8); // Incident Report
            userProfileInformationDao.deleteUserProfileInformationByProfileIdAndCategoryIdNotIn(profile.getId(), exemptCategories);
        }

        if (!userProfileInformationList.isEmpty()) {
            userProfileInformationDao.saveAll(userProfileInformationList);
        }

        return profile;
    }

    @CacheEvict(cacheNames = "user-cache", allEntries = true)
    public void deleteProfile(Integer id) {
        Optional<UserProfile> profileOptional = userProfileDao.findById(id);

        if (!profileOptional.isPresent())
            throw new NotFoundException("Cannot delete a profile that does not exist");

        userProfileDao.delete(profileOptional.get());
    }

    public List<UserProfileCategory> getCategories() {
        return userProfileCategoriesDao.findAll();
    }
}
