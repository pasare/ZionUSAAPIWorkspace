package org.zionusa.management.domain.userprofile;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "users_profile_search_view")
public class UserProfileSearchView implements BaseDomain<Integer> {

    /**
     * Base Properties
     */

    @Id
    @Column(name = "id")
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    /**
     * Base Properties
     */
    private Integer age;
    private Integer branchId;
    private String branchName;
    private Boolean children = false;
    private String branchPositions;
    private String city;
    private String companyCity;
    private Integer companyStateId;
    private String displayName;
    private Boolean enabled = false;
    private String ethnicity;
    private String faithLevel;
    private String gender;
    private Boolean gospelWorker = false;
    private Integer groupId;
    private String languages;
    private Integer mainBranchId;
    private Boolean married = false;
    private Integer parentBranchId;
    private String pictureUrl;
    private String professionalSkills;
    private Integer spiritualAge;
    private Integer roleId;
    private Boolean shortTermParticipant = false;
    private String software;
    private Integer spouseId;
    private Integer stateId;
    private Integer teamId;
    private Integer titleId;
    private Boolean worksRemotely = false;
}
