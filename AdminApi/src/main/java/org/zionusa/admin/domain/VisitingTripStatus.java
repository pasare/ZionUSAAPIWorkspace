package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.admin.util.constraints.DateFormatConstraint;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "visiting_trip_status")
public class VisitingTripStatus implements BaseDomain<Integer> {
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

    @NotNull(message = "The visitingTripId is required")
    private int visitingTripId;

    // Approval
    private Boolean approved;
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String approvedDate;
    private String approvedNotes;

    // Submission
    private Boolean submitted;
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String submittedDate;

    public VisitingTripStatus(VisitingTrip visitingTrip) {
        this.approved = false;
        this.approvedDate = null;
        this.approvedNotes = "";
        this.submitted = false;
        this.submittedDate = null;
        this.visitingTripId = visitingTrip.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VisitingTripStatus that = (VisitingTripStatus) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
