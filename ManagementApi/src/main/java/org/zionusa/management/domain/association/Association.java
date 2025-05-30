package org.zionusa.management.domain.association;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.management.domain.User;
import org.zionusa.management.domain.file.File;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "associations")
public class Association implements BaseDomain<Integer> {

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

    @NotNull
    private String name = "";

    /**
     * Relationships
     */

    private Integer leaderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderId", insertable = false, updatable = false)
    @ToString.Exclude
    private User leader;

    private Integer leaderTwoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderTwoId", insertable = false, updatable = false)
    @ToString.Exclude
    private User leaderTwo;

    private String pictureFileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pictureFileId", insertable = false, updatable = false)
    @ToString.Exclude
    private File picture;

    private String thumbnailFileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnailFileId", insertable = false, updatable = false)
    @ToString.Exclude
    private File thumbnail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Association that = (Association) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
