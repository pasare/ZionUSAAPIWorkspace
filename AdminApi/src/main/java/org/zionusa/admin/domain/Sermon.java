package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Nationalized;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "sermons")
public class Sermon implements BaseDomain<Integer> {

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

    @Nationalized
    private String bookName;

    @Nationalized
    private String pageNumber;

    @Nationalized
    private String title;

    private String date;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Nationalized
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private Subject subject;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "sermon_id")
    private List<SermonCategory> sermonCategories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Sermon sermon = (Sermon) o;
        return Objects.equals(id, sermon.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
