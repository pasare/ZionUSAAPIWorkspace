package org.zionusa.management.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ManagementAuthenticatedUser extends org.zionusa.base.util.auth.AuthenticatedUser {

    public ManagementAuthenticatedUser() {
        super("", "", new ArrayList<>());
    }

    public ManagementAuthenticatedUser(User user) {
        super(
                user.getUsername(),
                (user.getActiveDirectoryId() == null) ? "" : user.getActiveDirectoryId(), user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                AuthorityUtils.createAuthorityList(user.getAccess().getName())
        );
        this.setId(user.getId());
        this.setDisplayName(user.getDisplayName());
        this.setFirstName(user.getFirstName());
        this.setGender(user.getGender());
        this.setLastName(user.getLastName());
        this.setMiddleName(user.getMiddleName());
        this.setProfilePictureUrl(user.getProfilePicture() != null ? user.getProfilePicture().getPictureUrlMedium() : "");
        this.setRole(user.getRole().getName());
        this.setAccess(user.getAccess().getName());
        this.setTeamId(user.getTeamId());
        this.setGroupId(user.getTeam().getGroupId());
        this.setChurchId(user.getTeam().getGroup().getChurchId());
        this.setAssociationId(user.getTeam().getGroup().getChurch().getAssociationId());
        this.setParentChurchId(user.getTeam().getGroup().getChurch().getParentChurchId());
        this.setMainChurchId(user.getTeam().getGroup().getChurch().getMainChurchId());
        this.setTeamName(user.getTeam().getName());
        this.setGroupName(user.getTeam().getGroup().getName());
        this.setChurchName(user.getTeam().getGroup().getChurch().getName());
        this.setActiveDirectoryId(user.getActiveDirectoryId());
        this.setTeamEffectiveDate(user.getTeam().getEffectiveDate());
        this.setGroupEffectiveDate(user.getTeam().getGroup().getEffectiveDate());
        this.setTheologicalStudent(user.isTheologicalStudent());
        this.setTitleId(user.getTitleId());
        this.setLanguagePreference(user.getLanguagePreference());
        this.setGaGrader(Boolean.TRUE.equals(user.getGaGrader()));
        this.setReadyGrader(Boolean.TRUE.equals(user.getReadyGrader()));
        this.setTeacher(Boolean.TRUE.equals(user.getTeacher()));

        List<String> currentUserApplicationRoles = new ArrayList<>();
        if (user.getApplicationRoles() != null) {
            for (UserApplicationRole userApplicationRole : user.getApplicationRoles()) {
                currentUserApplicationRoles.add(userApplicationRole.getApplicationRole().getName());
            }
        }
        this.setUserApplicationRoles(currentUserApplicationRoles);
    }

}
