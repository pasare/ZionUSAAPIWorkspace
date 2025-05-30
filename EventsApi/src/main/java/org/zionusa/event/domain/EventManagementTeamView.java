package org.zionusa.event.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "event_management_team_view")
public class EventManagementTeamView implements BaseDomain<String> {

    @Id
    @Column(name = "id")
    @JsonIgnore
    private String id;
    private Boolean archived = false;
    private Boolean hidden = false;

    @JsonIgnore
    private String applicationRoleName;
    @JsonIgnore
    private String applicationRoleDisplayName;
    @JsonIgnore
    private Integer applicationRoleReferenceId;
    @JsonIgnore
    private Integer associationId;
    @JsonIgnore
    private Integer branchId;
    @JsonIgnore
    private String branchName;
    @JsonIgnore
    private Boolean enabled = true;
    @JsonIgnore
    private String gender;
    @JsonIgnore
    private Integer mainBranchId;
    @JsonIgnore
    private Integer parentBranchId;
    @JsonIgnore
    private Integer pictureId;
    @JsonIgnore
    private Integer roleId;
    @JsonIgnore
    private String roleName;
    @JsonIgnore
    private String username;

    // Visible fields
    private Integer applicationRoleId;
    private String displayName;
    private String pictureThumbnailUrl;
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventManagementTeamView that = (EventManagementTeamView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
