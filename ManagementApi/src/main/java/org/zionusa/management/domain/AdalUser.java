package org.zionusa.management.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdalUser {

    @EqualsAndHashCode.Include
    private String oid;
    private String username;

    public AdalUser(String username, String oid) {
        this.username = username;
        this.oid = oid;
    }
}
