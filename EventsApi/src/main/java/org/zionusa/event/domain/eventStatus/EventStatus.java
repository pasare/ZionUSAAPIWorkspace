package org.zionusa.event.domain.eventStatus;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;
import org.zionusa.event.domain.eventProposal.EventProposal;
import org.zionusa.event.util.ApplicationConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@AllArgsConstructor
@Table(name = "event_status")
public class EventStatus implements BaseDomain<Integer> {

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

    //@NotNull(message = "The eventProposalId is required")
    private int eventProposalId;

    @NotNull(message = "The requesterId is required")
    private int requesterId;

    private Boolean started;
    private Boolean finalized;
    private Boolean managerApproved;
    private Boolean adminApproved;
    @Deprecated
    private Boolean gaSubmitted;
    private Boolean gaApproved;
    private Boolean needsRevision;

    private String managerNotes;
    private String adminNotes;
    private String gaNotes;

    private Integer submitterId;
    private Integer managerApproverId;
    private Integer adminApproverId;
    private Integer gaApproverId;

    private String submitterName;
    private String managerApproverName;
    private String adminApproverName;
    private String gaApproverName;

    @DateFormatConstraint
    private String startedDate;

    @DateFormatConstraint
    private String finalizedDate;

    @DateFormatConstraint
    private String expectedManagerApprovalDate;

    @DateFormatConstraint
    private String actualManagerDecisionDate;

    @DateFormatConstraint
    private String expectedAdminApprovalDate;

    @DateFormatConstraint
    private String actualAdminDecisionDate;

    @DateFormatConstraint
    private String expectedGaApprovalDate;

    @DateFormatConstraint
    private String actualGaDecisionDate;

    public EventStatus(EventProposal eventProposal) {
        this.archived = false;
        this.eventProposalId = eventProposal.getId();
        this.requesterId = eventProposal.getRequesterId();
        this.started = true;

        // we will try two accepted formats timestamp and iso_local_date
        try {
            //convert proposed date to LocalDate so we can do other calculations
            LocalDate eventProposalDate = eventProposal.getProposedDate() != null ? LocalDate.parse(eventProposal.getProposedDate(), DateTimeFormatter.ISO_DATE) : LocalDate.now();
            LocalDate calculatedManagerApprovalDate = eventProposalDate.minusDays(ApplicationConstants.MANAGER_EXPECTED_APPROVAL_DAYS);
            LocalDate calculatedAdminApprovalDate = eventProposalDate.minusDays(ApplicationConstants.ADMIN_EXPECTED_APPROVAL_DAYS);
            LocalDate calculatedGaApprovalDate = eventProposalDate.minusDays(ApplicationConstants.GA_EXPECTED_APPROVAL_DAYS);

            // set the expected dates based on the default values provided from event management team
            this.startedDate = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE);
            this.expectedManagerApprovalDate = calculatedManagerApprovalDate.format(DateTimeFormatter.ISO_DATE);
            this.expectedAdminApprovalDate = calculatedAdminApprovalDate.format(DateTimeFormatter.ISO_DATE);
            this.expectedGaApprovalDate = calculatedGaApprovalDate.format(DateTimeFormatter.ISO_DATE);
        } catch (Exception e) {
            // something blew up with the date conversion
            throw new DateTimeException("Unable to parse the proposed start date from this event proposal");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventStatus that = (EventStatus) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
