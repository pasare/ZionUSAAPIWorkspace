package org.zionusa.management.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.management.domain.association.Association;
import org.zionusa.management.domain.state.State;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Deprecated
@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Proxy(lazy = false)
@Table(name = "churches")
public class Church implements BaseDomain<Integer> {

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

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean hiddenLocation;

    private Integer mainChurchId;

    @NotNull
    private Integer associationId;

    private Integer parentChurchId;

    private Integer leaderId;

    private Integer leaderTwoId;

    @NotNull
    private int typeId;

    @NotNull
    private String name;

    private String address;

    private String phone;

    private String email;

    private String city;

    @NotNull
    private Integer stateId;

    private String postalCode;

    private String latitude;

    private String longitude;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stateId", insertable = false, updatable = false)
    private State state;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typeId", insertable = false, updatable = false)
    private ChurchType type;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leaderId", insertable = false, updatable = false)
    private User leader;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "associationId", insertable = false, updatable = false)
    private Association association;

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("name ASC")
    @Where(clause = "archived=0")
    @JoinColumn(name = "churchId", insertable = false, updatable = false)
    @ToString.Exclude
    private List<Group> groups = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("name ASC")
    @JoinColumn(name = "parentChurchId", insertable = false, updatable = false)
    @ToString.Exclude
    private List<Church> branches = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Church church = (Church) o;
        return Objects.equals(id, church.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "church_display_view")
    public static class DisplayChurch {

        @Id
        @EqualsAndHashCode.Include
        private Integer id;
        private Boolean archived = false;
        private Integer associationId;
        private String associationName;
        private Integer mainChurchId;
        private Integer parentChurchId;
        private Integer leaderId;
        private String leaderName;
        private Integer leaderTwoId;
        private String leaderTwoName;
        private Integer typeId;
        private String type;
        private String name;
        private String address;
        private String city;
        private String email;
        private String latitude;
        private String longitude;
        private String phone;
        private String postalCode;
        private Integer stateId;
        private String stateAbbrv;
        private String stateName;
    }

    @Audited
    @Data
    @Entity
    @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
    @Table(name = "CHURCH_PICTURES")
    public static class ChurchPicture extends Auditable<String> {

        @Id
        @EqualsAndHashCode.Include
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        private Integer churchId;

        @NotNull
        private Integer typeId;
        private String name;
        private String description;
        private String pictureUrlFull;
        private String pictureUrlMedium;
        private String thumbnailUrl;

        @Transient
        private byte[] pictureFull;

        @Transient
        private byte[] pictureMedium;

        @Transient
        private byte[] thumbnail;

    }
}
