package org.zionusa.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Church {
    @EqualsAndHashCode.Include
    private Integer id;
    private Boolean archived = false;
    private Integer associationId;
    private String associationName;
    private Integer mainChurchId;
    private Integer parentChurchId;
    private Integer leaderId;
    private String leaderName;
    private Integer typeId;
    private String type;
    private String name;
    private String address;
    private String city;
    private Integer stateId;
    private String stateAbbrv;
    private String stateName;
}
