package org.zionusa.admin.domain.activities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "activities_church_report_view")
public class ChurchActivityReportView implements BaseDomain<String> {
    @Id
    @Column(name = "id")
    @JsonIgnore
    private String id;
    private Boolean archived = false;
    private Boolean hidden = false;

    private String activityAbbreviation;
    private String activityBackgroundColor;
    private Integer activityCount = 0;
    private Integer activityId;
    private String activityIconUrl;
    private String activityName;
    private Integer activityParticipantCount = 0;
    private String activityTextColor;
    private String activityTimeOfDay;
    private String categoryAbbreviation;
    private String categoryBackgroundColor;
    private Integer categoryId;
    private String categoryIconUrl;
    private String categoryName;
    private String categoryTextColor;
    private Integer churchId;
    private String churchName;
    private String churchCity;
    private String churchStateAbbrv;
    private Integer churchTypeId;
    private String churchType;
    private String date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChurchActivityReportView that = (ChurchActivityReportView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
