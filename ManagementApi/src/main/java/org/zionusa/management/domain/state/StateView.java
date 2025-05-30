package org.zionusa.management.domain.state;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "states_view")
public class StateView implements BaseDomain<Integer> {

    /**
     * Base Properties
     */

    @Id
    @Column(name = "id")
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;

    /**
     * Properties
     */

    private String fullName = "";
    private String shortName = "";

    /**
     * Relationships
     */

    private Integer countryId;
    private boolean countryArchived = false;
    private boolean countryHidden = false;
    private String countryFullName = "";
    private String countryShortName = "";
}
