package org.zionusa.base.domain;

import lombok.Data;

@Data
public class ApplicationRole {
    private Integer id;
    private String name;
    private String displayName;
    private String description;
    private Integer sortOrder;
}
