package org.zionusa.biblestudy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "studies")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Study implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    @Column(name = "archived", columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    @NotNull
    @Column(name = "study_type_id")
    private Integer studyTypeId;

    @Column(name = "study_category_id")
    private Integer studyCategoryId;

    private Integer bookNumber;

    private Integer chapterNumber;

    private Integer studyLevel;

    @NotNull
    private String title;

    @NotNull
    private String shortTitle;

    private boolean theological;

    private boolean beforeBaptism;

    @NotNull
    private Integer sortOrder;

    private Integer associatedStudy;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedDate;

    @OneToOne
    @JoinColumn(name = "study_type_id", insertable = false, updatable = false)
    private StudyType type;

    @OneToOne
    @JoinColumn(name = "study_category_id", insertable = false, updatable = false)
    private StudyCategory category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Study study = (Study) o;
        return Objects.equals(id, study.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
