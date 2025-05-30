package org.zionusa.management.domain.association;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Immutable
@Table(name = "associations_view")
public class AssociationView implements BaseDomain<Integer> {

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

    private String name = "";

    /**
     * Relationships
     */

    private Integer leaderId;
    private String leaderDisplayName;

    private Integer leaderTwoId;
    private String leaderTwoDisplayName;

    private String pictureFileId;
    private String thumbnailFileId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AssociationView that = (AssociationView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
