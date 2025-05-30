package org.zionusa.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Association {
    @EqualsAndHashCode.Include
    Integer id;
    String name;
    Integer leaderId;
    String leaderName;
    Integer sortOrder;
    Boolean enabled;
}
