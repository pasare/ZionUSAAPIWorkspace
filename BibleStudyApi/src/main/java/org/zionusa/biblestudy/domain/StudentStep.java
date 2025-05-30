package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Immutable
@Table(name = "student_step_view")
public class StudentStep {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;

    private String churchName;

    private String developRelationship;

    private Integer displayStep;

    private String firstName;

    private String lastName;

    private String gender;

    private String location; // city and state

    private Boolean lostSheep;

    private String pictureUrl;

    private String relationshipType;

    private Integer step;

    private String stepDate;

    private Integer userId1;

    private String userDisplayName1;

    private Integer userId2;

    private String userDisplayName2;

    private Integer userId3;

    private String userDisplayName3;
}
