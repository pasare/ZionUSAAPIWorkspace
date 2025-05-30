package org.zionusa.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode()
public class ApplicationRoleUserPermission implements BaseDomain<Integer> {
    private Integer id;

    private Integer userApplicationRoleId;
    private String userApplicationRoleDescription;
    private String userApplicationRoleDisplayName;
    private String userApplicationRoleName;

    private Integer userPermissionId;
    private String userPermissionDescription;
    private String userPermissionDisplayName;
    private String userPermissionName;

    public String getUserApplicationRoleName() {
        return userApplicationRoleName;
    }
}
