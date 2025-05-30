package org.zionusa.biblestudy.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "lre_user_pamphlets")
public class LREUserPamphlet implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "archived", columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    private String barcode;

    private Integer userId;

    private String userDisplayName;

    private Integer churchId;

    private String churchName;

    private String imageUrl;

    private Integer parentChurchId;

    private Boolean goodCondition = true;

    private Boolean lost = false;

    private Boolean destroyed = false;

    private Boolean returned;

    @DateFormatConstraint
    private String receivedDate;

    private Integer pamphletId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pamphletId", insertable = false, updatable = false)
    private LREPamphlet lrePamphlet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LREUserPamphlet that = (LREUserPamphlet) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
