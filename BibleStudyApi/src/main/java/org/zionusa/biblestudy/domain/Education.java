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
@Table(name = "educations")
public class Education {

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer id;

    private Integer educationTypeId;

    private String name;

    private Date startDate;

    private Date endDate;

    private Date createdDate;

    private Date updatedDate;
}
