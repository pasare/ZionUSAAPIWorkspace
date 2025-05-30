package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Audited
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "preaching_log_movement_goals")
public class PreachingLogMovementGoals {
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer acquaintances;

    @NotNull
    private Integer contacts;

    @NotNull
    private Integer coWorkers;

    @NotNull
    private Integer family;

    @NotNull
    private Integer friends;

    @NotNull
    private Integer meaningful;

    @NotNull
    private Integer movementId;

    @NotNull
    private Integer movementTypeId;

    @NotNull
    private Integer neighbors;

    @NotNull
    private Integer simple;

    @NotNull
    private Integer referenceId;

}
