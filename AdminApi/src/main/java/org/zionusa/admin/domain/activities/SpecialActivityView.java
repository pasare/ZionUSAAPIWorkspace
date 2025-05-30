package org.zionusa.admin.domain.activities;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "activities_special_view")
public class SpecialActivityView implements BaseDomain<Integer> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    private String categoryAbbreviation;

    private String categoryBackgroundColor;

    private String categoryDescription;

    private Boolean categoryHidden;

    private Integer categoryId;

    private String categoryIconUrl;

    private String categoryName;

    private String categoryTextColor;

    private String date;

    private String name;

    private Integer orderId;

    private Integer specialDayId;

    private String timeOfDay;

    private Integer weight;

    @Transient
    private Boolean readOnly = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SpecialActivityView that = (SpecialActivityView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
