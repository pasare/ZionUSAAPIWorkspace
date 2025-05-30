package org.zionusa.biblestudy.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateTimeFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@Table(name = "preaching_materials")
public class PreachingMaterial implements BaseDomain<Integer> {

    /**
     * Base Properties
     */

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

    /**
     * Properties
     */

    @DateTimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String createdDate;

    private String name = "";

    private String url = "";

    @DateTimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String validFrom;

    @DateTimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String validUntil;
}
