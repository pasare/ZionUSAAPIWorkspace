package org.zionusa.preaching.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partner {

    private Integer id;

    private Integer teamId;

    private Integer groupId;

    private Integer churchId;

    private Integer parentChurchId;

}
