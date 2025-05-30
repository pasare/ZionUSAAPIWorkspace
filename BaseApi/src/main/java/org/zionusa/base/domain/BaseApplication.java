package org.zionusa.base.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode()
public class BaseApplication implements BaseDomain<Integer> {
    private Integer id;
    private boolean archived = false;
    private boolean hidden = false;
    private String appleAppStoreUrl;
    private String description;
    private Boolean enabled = true;
    private String googlePlayStoreUrl;
    private String iconTitle;
    private String iconUrl;
    private String launchUrl;
    private String name;
    private String uniqueId;
}
