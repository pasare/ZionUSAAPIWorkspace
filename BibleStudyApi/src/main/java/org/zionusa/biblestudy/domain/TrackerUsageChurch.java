package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Audited
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tracker_active_users_church_view")
public class TrackerUsageChurch {
    @Id
    private String id;

    private Integer year;

    private Integer month;

    private Integer churchId;

    private String churchName;

    private Integer numOfActiveUsers;
}
