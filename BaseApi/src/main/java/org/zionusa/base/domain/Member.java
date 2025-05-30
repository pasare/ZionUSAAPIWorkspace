package org.zionusa.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Member {
    @EqualsAndHashCode.Include
    private Integer id;
    private Integer associationId;
    private Integer churchId;
    private String churchName;
    private String displayName;
    private String gender;
    private Integer groupId;
    private String groupName;
    private Integer mainChurchId;
    private Integer parentChurchId;
    private String pictureUrl;
    private Integer teamId;
    private String teamName;
}
