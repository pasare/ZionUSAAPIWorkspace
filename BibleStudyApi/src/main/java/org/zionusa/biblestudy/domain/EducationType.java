package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "education_types")
public class EducationType {

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer id;

    private String name;

    private String description;

    private Date createdDate;

    private Date updatedDate;
}
