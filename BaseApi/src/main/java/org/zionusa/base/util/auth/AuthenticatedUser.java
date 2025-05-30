package org.zionusa.base.util.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AuthenticatedUser extends User {
    @EqualsAndHashCode.Include
    private Integer id;

    private String access;
    private String activeDirectoryId;
    private Integer associationId;
    @Transient
    private Integer branchId;
    @Transient
    private String branchName;
    private Integer churchId;
    private String churchName;
    private String displayName;
    private String firstName;
    private boolean gaGrader;
    private String gender;
    private Date groupEffectiveDate;
    private Integer groupId;
    private String groupName;
    private String languagePreference;
    private String lastName;
    private Integer mainChurchId;
    private String middleName;
    private Integer parentChurchId;
    private String profilePictureUrl;
    private boolean readyGrader;
    private String role;
    private List<String> userApplicationRoles;
    private boolean teacher;
    private Date teamEffectiveDate;
    private Integer teamId;
    private String teamName;
    private boolean theologicalStudent;
    private Integer titleId;

    public AuthenticatedUser() {
        super("", "", new ArrayList<>());
    }

    public AuthenticatedUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthenticatedUser(String username, String s, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, List<GrantedAuthority> authorityList) {
        super(username, s, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorityList);
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getTeamEffectiveDate() {
        return teamEffectiveDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getGroupEffectiveDate() {
        return groupEffectiveDate;
    }

    public Integer getId() {
        return id;
    }

    public String getAccess() {
        return access;
    }

    public Integer getAssociationId() {
        return associationId;
    }

    public Integer getChurchId() {
        return churchId;
    }

    public String getChurchName() {
        return churchName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getRole() {
        return role;
    }

    public Integer getParentChurchId() {
        return parentChurchId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public List<String> getUserApplicationRoles() {
        return userApplicationRoles;
    }

    public boolean isTheologicalStudent() {
        return theologicalStudent;
    }
}
