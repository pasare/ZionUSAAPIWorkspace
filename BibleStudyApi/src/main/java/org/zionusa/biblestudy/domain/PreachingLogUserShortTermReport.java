package org.zionusa.biblestudy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "preaching_log_user_short_term_daily_view")
public class PreachingLogUserShortTermReport {
    public static final Integer DEFAULT_LIMIT = 150;

    @Id
    @EqualsAndHashCode.Include
    @JsonIgnore
    private String uniqueId;

    private Integer shortTermId;

    @JsonIgnore
    private String date;

    private Integer userId;

    private String userDisplayName;

    private String userPictureUrl;

    private Integer churchId;

    private String churchName;

    private Integer mainChurchId;

    private Integer fruit;

    private Integer meaningful;

    private Integer simple;

    @Transient
    private Integer rank;

}
