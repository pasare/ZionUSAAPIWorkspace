package org.zionusa.event.domain.resultsSurvey;

import lombok.*;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "reminder_event_result_survey_table_view")
public class ReminderEventResultSurveyTableView implements BaseDomain<Integer> {
    @Id
    @Column(name = "eventId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    private String eventTitle;
    private String proposedEndDate;
    private String requesterId;
    private String requesterName;
    private String requesterEmail;
    private String branchId;
    private String branchName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReminderEventResultSurveyTableView that = (ReminderEventResultSurveyTableView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
