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
@Table(name = "preaching_log_user_daily_view")
public class PreachingLogUserReport {
    public static final Integer DEFAULT_LIMIT = 150;

    @Id
    @EqualsAndHashCode.Include
    @JsonIgnore
    private String uniqueId;

    @JsonIgnore
    private String date;

    private Integer userId;

    private String userDisplayName;

    private String userPictureUrl;

    private Integer churchId;

    private String churchName;

    private Integer mainChurchId;

    private Integer fruit = 0;

    private Integer meaningful = 0;

    private Integer simple = 0;

    @Transient
    private Integer rank;

}
