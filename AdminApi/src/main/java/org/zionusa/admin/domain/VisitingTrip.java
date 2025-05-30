package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.admin.util.constraints.DateFormatConstraint;
import org.zionusa.admin.util.constraints.TimeFormatConstraint;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "visiting_trips")
public class VisitingTrip implements BaseDomain<Integer> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    @NotNull(message = "The arrival date is required")
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String arrivalDate;

    @NotNull(message = "The arrival time is required")
    @TimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private String arrivalTime;

    @NotNull(message = "The departure date is required")
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String departureDate;

    @NotNull(message = "The departure time is required")
    @TimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private String departureTime;

    private String distance;

    private String foodAllergies;

    private String purpose;

    private Integer memberCount;


    // Church Information
    @NotNull(message = "The home zion id is required")
    private Integer homeZionId;
    private String homeZionName;
    private String homeZionCity;
    private String homeZionStateProvince;

    @NotNull(message = "The visiting zion id is required")
    private Integer visitingZionId;
    private String visitingZionName;
    private String visitingZionCity;
    private String visitingZionStateProvince;


    // Church Leader Information
    @NotNull(message = "The church leader id is required")
    private Integer churchLeaderOneId;
    private String churchLeaderOneDisplayName;
    private String churchLeaderOneThumbnailUrl;
    private Boolean churchLeaderOneComing;

    private Integer churchLeaderTwoId;
    private String churchLeaderTwoDisplayName;
    private String churchLeaderTwoThumbnailUrl;
    private Boolean churchLeaderTwoComing;


    // Member Information
    // TODO: Later, break out into its own table
    private Boolean haveCareTakers;
    private Boolean haveNewMembers;
    private Boolean haveMission;
    private Boolean haveOnlyChurchLeaders;
    private Boolean haveOther;
    private String haveOtherMessage;


    // Approval Process
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "visitingTripStatusId")
    private VisitingTripStatus visitingTripStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VisitingTrip that = (VisitingTrip) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
