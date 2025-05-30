package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Table(name = "short_term_preaching_view")
public class ShortTermPreachingView {
    @Id
    private Integer id;
    private String name;
    private String city;
    private Integer stateId;
    private String startDate;
    private String endDate;
    private Boolean enabled = true;
}
