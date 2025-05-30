package org.zionusa.event.domain.eventvolunteer;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.event.domain.eventProposal.EventProposal;
import org.zionusa.event.domain.resultsSurvey.ResultsSurvey;
import org.zionusa.event.domain.VolunteerType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@AllArgsConstructor
@Table(
    name = "event_volunteer_management",
    uniqueConstraints = {@UniqueConstraint(
        columnNames = {"branchId", "eventProposalId", "type", "resultsSurveyId", "isForResultsSurvey"
        })}
)
public class EventVolunteerManagement implements BaseDomain<Integer> {
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

    private Integer femaleAdults = 0;
    private Integer femaleCollegeStudents = 0;
    private Integer femaleTeenagers = 0;
    private Integer femaleYoungAdults = 0;
    private Integer maleAdults = 0;
    private Integer maleCollegeStudents = 0;
    private Integer maleTeenagers = 0;
    private Integer maleYoungAdults = 0;
    private Integer total = 0;
    @NotNull
    private VolunteerType type;
    @NotNull
    private Boolean helpingBranch;
    @NotNull
    private Integer branchId;
    @NotNull
    private String branchName;
    // @NotNull = TODO
    @Column(columnDefinition = "bit default 0")
    private Boolean isForResultsSurvey = false;

    /**
     * Relationships
     */

    @NotNull
    private Integer eventProposalId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventProposalId", insertable = false, updatable = false)
    @ToString.Exclude
    private EventProposal eventProposal;

    private Integer resultsSurveyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resultsSurveyId", insertable = false, updatable = false)
    @ToString.Exclude
    private ResultsSurvey resultsSurvey;

    public EventVolunteerManagement(EventProposal eventProposal, VolunteerType type) {
        this.setBranchId(eventProposal.getBranchId());
        this.setBranchName(eventProposal.getBranchName());
        this.setEventProposalId(eventProposal.getId());
        this.setHelpingBranch(false);
        this.setType(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventVolunteerManagement that = (EventVolunteerManagement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
