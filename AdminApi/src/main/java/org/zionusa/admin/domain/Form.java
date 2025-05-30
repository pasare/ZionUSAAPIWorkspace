package org.zionusa.admin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
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
@Table(name = "forms")
public class Form implements BaseDomain<Integer> {

    @Transient
    Permission permission;
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
    private String title;
    private String url;
    private String description;
    private String shortDescription;
    private boolean standard;
    private Date dueDate;
    private Date createdDate;
    private Date updatedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Form form = (Form) o;
        return Objects.equals(id, form.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
