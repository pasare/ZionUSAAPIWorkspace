package org.zionusa.management.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventApprover {

    @EqualsAndHashCode.Include
    private Integer id;
    private String email;
    private String name;
    private Integer associationId;
    private Integer branchId;
    private String branchName;
    private String pictureUrl;
    private User.UserPicture profilePicture;


    EventApprover(User.ApplicationRole user) {
        this.id = user.getId();
        this.email = user.getUserName();
        this.name = user.getDisplayName();
        this.associationId = user.getAssociationId();
        this.branchId = user.getChurchId();
        this.branchName = user.getChurchName();
        this.pictureUrl = user.getPictureUrl();
        this.profilePicture = user.getProfilePicture();
    }
}
