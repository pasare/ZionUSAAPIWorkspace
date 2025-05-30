package org.zionusa.admin.domain.activities;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "activities_categories_view")
public class ActivityCategoryView implements BaseDomain<Integer> {
    @Id
    @Column(name = "id")
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    private String abbreviation;
    private Boolean approved;
    private Integer approverId;
    private String attachmentUrl;
    private String backgroundColor;
    private String backgroundUrl;
    private String categoryAbbreviation;
    private String categoryBackgroundColor;
    private String categoryDescription;
    private String categoryIconUrl;
    private Integer categoryId;
    private String categoryName;
    private String categoryTextColor;
    private Integer churchId;
    //    private String churchName;
    private String description;
    private Integer editorId;
    private String feedback;
    private Integer groupId;
    private String iconUrl;
    private String link;
    private String logoUrl;
    private String name;
    private String notes;
    private String notificationDateAndTime;
    private Integer objectId;
    private Double points = 0.0;
    private Boolean published;
    private String publishedDate;
    private Integer requesterId;
    private String tag;
    private Integer teamId;
    private String textColor;
    private Boolean useForNotifications;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ActivityCategoryView that = (ActivityCategoryView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
