package org.zionusa.admin.domain.activities;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "activities_group_log_view")
public class GroupActivityLogView implements BaseDomain<Integer> {
    @Id
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    private Integer activityId;
    private Boolean activityApproved;
    private Integer activityApproverId;
    private Boolean activityArchived;
    private String activityAttachmentUrl;
    private String activityBackgroundUrl;
    private Integer activityChurchId;
    private String activityDate;
    private Integer activityEditorId;
    private String activityDescription;
    private String activityFeedback;
    private Integer activityGroupId;
    private String activityLink;
    private String activityLogoUrl;
    private String activityName;
    private String activityNotificationDateAndTime;
    private Integer activityObjectId;
    private Boolean activityPublished;
    private String activityPublishedDate;
    private String activityNotes;
    private Integer activityPoints;
    private Integer activityRequesterId;
    private String activityTag;
    private Integer activityTeamId;
    private Boolean activityUseForNotifications;
    private String categoryAbbreviation;
    private String categoryBackgroundColor;
    private String categoryDescription;
    private Integer categoryId;
    private String categoryIconUrl;
    private String categoryName;
    private String categoryTextColor;
    private String notes;
    private Integer orderId;
    private Integer participantCount;
    private Integer planChurchId;
    private String planDate;
    private Integer planGroupId;
    private Integer planId;
    private String planNotes;
    private Boolean planSubmitted;
    @Transient
    private Boolean readOnly;
    private String timeOfDay;
    private Integer weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupActivityLogView that = (GroupActivityLogView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
