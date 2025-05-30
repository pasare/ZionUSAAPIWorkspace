package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Immutable
@Table(name = "preaching_log_region_daily_view")
public class MovementRegionGoal {
    @Id
    @EqualsAndHashCode.Include
    private String uniqueId;

    private Date date;

    private Integer regionId;

    private String name;

    private String city;

    private String churchName;

    private String stateAbbrev;

    private String stateName;

    private Integer mainChurchId;

    private Integer associationId;

    private Integer acquaintances;

    private Integer contacts;

    private Integer coWorkers;

    private Integer family;

    private Integer friends;

    private Integer fruit;

    private Integer meaningful;

    private Integer neighbors;

    private Integer simple;

    private Integer numLogs;

}
