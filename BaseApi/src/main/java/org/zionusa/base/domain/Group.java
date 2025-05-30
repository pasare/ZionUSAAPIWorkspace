package org.zionusa.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Group {
    @EqualsAndHashCode.Include
    private Integer id;
    private Integer leaderId;
    private String name;
    private Boolean churchGroup;
}
