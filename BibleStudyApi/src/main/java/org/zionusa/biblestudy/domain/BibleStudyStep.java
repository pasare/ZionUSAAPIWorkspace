package org.zionusa.biblestudy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "bible_studies_steps", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class BibleStudyStep implements BaseDomain<Integer> {

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
    private String displayName;

    @NotNull
    private String name;

    private String translationKey;

    @NotNull
    private Integer step;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BibleStudyStep that = (BibleStudyStep) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
