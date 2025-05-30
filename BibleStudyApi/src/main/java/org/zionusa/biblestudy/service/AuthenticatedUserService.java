package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.biblestudy.dao.BibleStudyDao;
import org.zionusa.biblestudy.dao.SermonRecordingDao;
import org.zionusa.biblestudy.domain.BibleStudy;
import org.zionusa.biblestudy.domain.SermonRecording;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthenticatedUserService {

    private final SermonRecordingDao sermonRecordingDao;

    private final BibleStudyDao bibleStudyDao;

    public static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    public AuthenticatedUserService(SermonRecordingDao sermonRecordingDao, BibleStudyDao bibleStudyDao) {
        this.sermonRecordingDao = sermonRecordingDao;
        this.bibleStudyDao = bibleStudyDao;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser, null, authenticatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null)
            return (AuthenticatedUser) authentication.getPrincipal();

        return null;
    }

    public boolean canAccessSermonRecording(AuthenticatedUser authenticatedUser, Integer churchId, Integer groupId,
                                            Integer teamId, Integer userId) {
        switch (authenticatedUser.getAccess()) {
            case "Admin":
                return true;
            case "Overseer":
            case "Church":
                /*if (churchId != null) {
                    return isChurchAdmin(authenticatedUser, churchId);
                } */
                if (userId != null) {
                    List<SermonRecording> sermonRecordings = sermonRecordingDao.findByPreacherId(userId);

                    if (!sermonRecordings.isEmpty()) {
                        SermonRecording sermonRecording = sermonRecordings.get(0);
                        return authenticatedUser.getChurchId().equals(sermonRecording.getChurchId())
                                || authenticatedUser.getChurchId().equals(sermonRecording.getParentChurchId());
                    }
                    return false;
                }
            case "Group":
                return isGroupAdmin(authenticatedUser, groupId);
            case "Team":
                return isTeamAdmin(authenticatedUser, teamId);
            case "Member":
                return authenticatedUser.getId().equals(userId);
        }
        return false;
    }

    public boolean canModifySermonRecording(AuthenticatedUser authenticatedUser, SermonRecording sermonRecording) {
        switch (authenticatedUser.getAccess()) {
            case "Admin":
                return true;
            case "Overseer":
            case "Church":
                return isChurchAdmin(authenticatedUser, sermonRecording.getParentChurchId(), sermonRecording.getChurchId());
            case "Group":
                return isGroupAdmin(authenticatedUser, sermonRecording.getGroupId());
            case "Team":
                return isTeamAdmin(authenticatedUser, sermonRecording.getTeamId());
            case "Member":
                return authenticatedUser.getId().equals(sermonRecording.getPreacherId());
        }
        return false;
    }

    public boolean canApproveBibleStudy(AuthenticatedUser authenticatedUser, Integer bibleStudyId) {
        Optional<BibleStudy> bibleStudyOptional = bibleStudyDao.findById(bibleStudyId);

        if (bibleStudyOptional.isPresent()) {
            BibleStudy bibleStudy = bibleStudyOptional.get();

            switch (authenticatedUser.getAccess()) {
                case "Admin":
                    return true;
                case "Church":
                    return isChurchAdmin(authenticatedUser, bibleStudy.getParentChurchId(), bibleStudy.getChurchId());
                case "Group":
                    return isGroupAdmin(authenticatedUser, bibleStudy.getGroupId());
            }
        }

        return false;
    }

    private boolean isChurchAdmin(AuthenticatedUser authenticatedUser, Integer parentChurchId, Integer churchId) {
        Integer userChurchId = authenticatedUser.getChurchId();
        return churchId != null && (userChurchId.equals(parentChurchId) || userChurchId.equals(churchId));
    }

    private boolean isGroupAdmin(AuthenticatedUser authenticatedUser, Integer groupId) {
        return groupId != null && groupId.equals(authenticatedUser.getGroupId());
    }

    private boolean isTeamAdmin(AuthenticatedUser authenticatedUser, Integer teamId) {
        return teamId != null && teamId.equals(authenticatedUser.getTeamId());
    }
}
