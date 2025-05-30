package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Immutable
@Table(name = "movements_goals_church_view")
public class MovementChurchGoal {
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer id;

    private Integer referenceId;

    private String name;

    private Integer regionId;

    private String regionName;

    private Integer referenceTypeId;

    private Integer movementGoalTypeId;

    private String movementGoalTypeName;

    private Integer movementId;

    private String movementName;

    private String movementStartDate;

    private String movementEndDate;

    private Integer participantCount;

    private Integer fruit;

    private Integer oneTimeAttendance;

    private Integer meaningful;

    private Integer simple;

    private Integer talents;

    public static MovementChurchGoal addForRegion(MovementChurchGoal a, MovementChurchGoal b) {
        MovementChurchGoal c = new MovementChurchGoal();
        c.setRegionId(b.getRegionId());
        c.setId(b.getId());
        // Ignore date
        c.setReferenceId(b.getReferenceId());
        c.setReferenceTypeId(b.getReferenceTypeId());
        c.setName(b.getName());
        c.setRegionName(b.getRegionName());
        c.setMovementId(b.getMovementId());
        c.setMovementName(b.getMovementName());

        c.setMovementStartDate(b.getMovementStartDate());
        c.setMovementGoalTypeName(b.getMovementGoalTypeName());
        c.setMovementGoalTypeId(b.getMovementGoalTypeId());
        c.setMovementEndDate(b.getMovementEndDate());

        // Preaching
        c.setMeaningful(a.getMeaningful() + b.getMeaningful());
        c.setSimple(a.getSimple() + b.getSimple());
        c.setTalents(a.getTalents() + b.getTalents());
        c.setFruit(a.getFruit() + b.getFruit());
        c.setParticipantCount(a.getParticipantCount() + b.getParticipantCount());
        c.setFruit(a.getFruit() + b.getFruit());
        c.setMeaningful(a.getMeaningful() + b.getMeaningful());
        c.setOneTimeAttendance(a.getOneTimeAttendance() + b.getOneTimeAttendance());
        c.setSimple(a.getSimple() + b.getSimple());
        return c;
    }


}
