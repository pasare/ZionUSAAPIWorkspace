package org.zionusa.event.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.event.domain.contact.Contact;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "locations")
@Audited(targetAuditMode = NOT_AUDITED)
public class Location implements BaseDomain<Integer> {

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

    @NotEmpty(message = "The locationName is required")
    private String locationName;

    private String abbreviation;

    private String address;

    private String city;

    private String stateProvince;

    private String countryRegion;

    private String description;

    private Boolean permits;

    private String permitDeadline;

    private String indoorOutdoor;

    @Column(name = "point_of_contact_id")
    private Integer pointOfContactId;

    private String zipPostalCode;

    @OneToOne
    @JoinColumn(name = "point_of_contact_id", insertable = false, updatable = false)
    private Contact pointOfContact;

    @NotNull(message = "deletable status is required")
    private boolean deletable = true;

    private String facebookUrl;

    private String instagramUrl;

    private String linkedInUrl;

    private String twitterUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
