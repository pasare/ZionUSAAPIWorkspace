package org.zionusa.biblestudy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateTimeFormatConstraint;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "bible_studies_progress", uniqueConstraints = {@UniqueConstraint(columnNames = {"bible_study_id"})})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class BibleStudyProgress implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @javax.validation.constraints.NotNull
    @Column(name = "archived", columnDefinition = "bit default 0")
    private Boolean archived = false;

    @javax.validation.constraints.NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    @NotNull
    @Column(name = "bible_study_id")
    private Integer bibleStudyId;

    @NotNull
    @Column(name = "bible_study_step_id")
    private Integer bibleStudyStepId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bible_study_step_id", insertable = false, updatable = false)
    @ToString.Exclude
    private BibleStudyStep bibleStudyStep;

    private Integer approvedById;

    private String approvedByName;

    @DateTimeFormatConstraint
    private String updatedDateTime;

    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BibleStudyProgress that = (BibleStudyProgress) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
