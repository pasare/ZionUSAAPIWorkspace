package org.zionusa.management.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.enums.EUserType;
import org.zionusa.management.domain.access.Access;
import org.zionusa.management.domain.role.Role;
import org.zionusa.management.domain.title.Title;
import org.zionusa.management.domain.userprofile.UserProfile;
import org.zionusa.management.domain.usertype.UserType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users")
public class User implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean hidden = false;

    private String activeDirectoryId;

    @NotNull
    private int teamId;

    @NotNull
    private int accessId;

    @NotNull
    private int roleId;

    private Integer pictureId;

    @NotNull
    private int titleId;

    @NotNull
    private boolean married;

    @NotNull
    private String firstName;

    private String lastName;

    private String middleName;

    @Transient
    private String displayName;

    @Column(name = "user_name", unique = true)
    private String username;

    @Column(unique = true)
    private String recoveryEmail;

    @NotNull
    private String gender;

    private Integer birthYear;

    private Integer baptismYear;

    private Integer spouseId;

    private String languagePreference;

    private Boolean gospelWorker;

    private boolean theologicalStudent;

    private String lastLoginDate;

    @NotNull
    private boolean enabled = true;

    @NotNull
    @Column(name = "account_not_expired")
    private boolean accountNonExpired;

    @NotNull
    @Column(name = "credentials_not_expired")
    private boolean credentialsNonExpired;

    @NotNull
    @Column(name = "account_not_locked")
    private boolean accountNonLocked;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean gaGrader;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean teacher;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean readyGrader;

    @OneToOne
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "accessId", insertable = false, updatable = false)
    private Access access;

    @OneToOne
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "roleId", insertable = false, updatable = false)
    private Role role;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private List<UserApplicationRole> applicationRoles;

    @OneToOne
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "titleId", insertable = false, updatable = false)
    private Title title;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId", insertable = false, updatable = false)
    @ToString.Exclude
    private Team team;

    @NotNull()
    @Enumerated(EnumType.STRING)
    private EUserType typeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId", insertable = false, updatable = false)
    @ToString.Exclude
    private UserType type;

    @Transient
    private UserProfile profile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pictureId", insertable = false, updatable = false)
    private UserPicture profilePicture;

    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        if (title != null) {
            String shortTitle = title.getDisplayName();
            sb.append(shortTitle).append(" ");
        }
        sb.append(firstName).append(" ");
        if (middleName != null && !middleName.equals("")) {
            sb.append(middleName.charAt(0)).append(". ");
        }
        if (lastName != null) sb.append(lastName);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "users_application_roles_materialized_view_2020_12")
    public static class ApplicationRole {

        @Id
        @EqualsAndHashCode.Include
        @JsonIgnore
        private String userApplicationRoleId;
        private Integer id;
        private String displayName;
        private String userName;
        private String applicationRoleDisplayName;
        private Integer applicationRoleId;
        private String applicationRoleName;
        private Integer associationId;
        private Integer churchId;
        private String churchName;
        private String churchCity;
        private String churchState;
        private String churchShortState;
        private Integer mainChurchId;
        private Integer referenceId;

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "pictureId", insertable = false, updatable = false)
        @JsonIgnore
        private UserPicture profilePicture;

        @Transient
        private String pictureUrl;

        public String getPictureUrl() {
            if (profilePicture == null) {
                return null;
            }
            return profilePicture.thumbnailUrl;
        }
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Audited(targetAuditMode = NOT_AUDITED)
    @Table(name = "users_display_view")
    public static class DisplayUser {

        @Id
        @EqualsAndHashCode.Include
        private Integer id;
        private Integer associationId;
        private Integer mainChurchId;
        private Integer parentChurchId;
        private Integer churchId;
        private Integer groupId;
        private Integer teamId;
        private String displayName;
        private String churchName;
        private String groupName;
        private String teamName;
        private String gender;
        private String username;
        private Integer accessId;
        private String accessName;
        private Integer roleId;
        private String roleName;
        private Boolean enabled = true;
        private boolean readyGrader;
        private boolean gaGrader;
        private boolean teacher;
        private String typeId;
        private String typeName;

        @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
        @JoinColumn(name = "userId", insertable = false, updatable = false)
        @JsonIgnore
        private List<UserApplicationRole> userApplicationRoles;

        @Transient
        private List<String> applicationRoles;
        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "pictureId", insertable = false, updatable = false)
        @JsonIgnore
        private UserPicture profilePicture;
        @Transient
        private String pictureUrl;

        public List<String> getApplicationRoles() {
            if (userApplicationRoles == null) {
                return new ArrayList<>();
            }
            return userApplicationRoles
                .stream()
                .map(r -> r.getApplicationRole().getDisplayName())
                .collect(Collectors.toList());
        }

        public String getPictureUrl() {
            if (profilePicture == null) {
                return null;
            }
            return profilePicture.thumbnailUrl;
        }
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "users_profile_view")
    public static class ProfileUser {

        @Id
        @EqualsAndHashCode.Include
        private Integer id;
        private Integer accessId;
        private Integer associationId;
        private Integer baptismYear;
        private Integer birthYear;
        private Integer churchId;
        private String churchName;
        private String displayName;
        private Boolean enabled = true;
        private String firstName;
        private boolean gaGrader;
        private String gender;
        private Boolean gospelWorker;
        private Integer groupId;
        private String groupName;
        private String lastLoginDate;
        private String languagePreference;
        private String lastName;
        private Integer mainChurchId;
        private boolean married;
        private String middleName;
        private Integer parentChurchId;
        private Integer pictureId;
        private boolean readyGrader;
        private Integer roleId;
        private Integer spouseId;
        private boolean teacher;
        private Integer teamId;
        private String teamName;
        private boolean theologicalStudent;
        private Integer titleId;
        private String typeId;
        private String typeName;
        private String username;

        @Transient
        private UserProfile profile;

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "pictureId", insertable = false, updatable = false)
        private UserPicture profilePicture;
    }

    @Data
    @Entity
    @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
    @Audited
    @Table(name = "USER_PICTURES")
    public static class UserPicture extends Auditable<String> {

        @Id
        @EqualsAndHashCode.Include
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        private Integer userId;
        private String name;
        private String description;
        private String pictureUrlFull;
        private String pictureUrlMedium;
        private String thumbnailUrl;

        @Transient
        @JsonIgnore
        private byte[] pictureFull;

        @Transient
        @JsonIgnore
        private byte[] pictureMedium;

        @Transient
        @JsonIgnore
        private byte[] thumbnail;

    }
}
