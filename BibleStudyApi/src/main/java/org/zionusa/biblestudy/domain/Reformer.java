package org.zionusa.biblestudy.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "reformers")
public class Reformer implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "archived", columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    private Integer userId;

    private Integer parentChurchId;

    private Integer churchId;

    private Integer groupId;

    private Integer teamId;

    private Integer currentReformerTypeId = 1;

    private Integer futureReformerTypeId = 1;

    private String name;

    private String churchName;

    private String imageUrl;

    private String gender;

    private String advancementReasons;

    private String status;

    @DateFormatConstraint
    private String startDate;

    @DateFormatConstraint
    private String endDate;

    //level 1
    private Integer weeklyPreachingCount = 0;

    private Boolean tithing = true;

    private Boolean fullAttendance = true;

    private Boolean levelOneSermonBook = false;


    //level 2
    private Integer weeklyBibleStudies = 0;

    private Boolean patriotPledgeSigned = false;


    //level 3
    private Boolean lreTimelineTest = false;

    private Boolean lreTeacher = false;

    private Boolean lreTimelineBrochure = false;

    //level 4
    private Boolean fruitBecomesVolunteer = false;

    private Boolean contactHasFiveBibleStudies = false;

    private Boolean lreCenturiesTest = false;

    private Integer weeklyFruitCount = 0;

    private Boolean bearFruit = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "futureReformerTypeId", insertable = false, updatable = false)
    private ReformerType futureReformerType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currentReformerTypeId", insertable = false, updatable = false)
    private ReformerType currentReformerType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Reformer reformer = (Reformer) o;
        return Objects.equals(id, reformer.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
