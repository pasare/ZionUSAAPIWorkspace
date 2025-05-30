package org.zionusa.event.domain.eventRegistration;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;
import org.zionusa.base.util.constraints.TimeFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "event_registrations")
public class EventRegistration implements BaseDomain<Integer> {

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

    @NotNull(message = "The eventProposalId is required")
    private Integer eventProposalId;

    @NotNull(message = "The participantId is required")
    private Integer participantId;

    private String participantName;

    @NotNull
    private String participantEmail;

    private String participantBranch;

    private String qrCode;

    private String qrImageUrl;

    private Boolean sendConfirmationEmail = false;

    private String gender;

    private String ageRange;

    private String tShirtSize;

    private String phoneNumber;

    private String company;

    private String invitedBy;

    private Integer invitedById;

    private Boolean subscribeToNewsletter = false;

    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String eventDate;

    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String registrationDate;

    private Boolean confirmed;

    private Boolean attended;

    private String participantType;

    private Boolean cancelled;

    private String cancelledReason;

    @TimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private String arrivalTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventRegistration that = (EventRegistration) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
