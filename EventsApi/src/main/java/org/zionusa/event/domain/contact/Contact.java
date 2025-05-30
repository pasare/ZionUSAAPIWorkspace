package org.zionusa.event.domain.contact;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@AllArgsConstructor
@Table(name = "contacts")
public class Contact implements BaseDomain<Integer> {

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

    @NotBlank(message = "The lastName of the contact is required")
    private String lastName;

    @NotBlank(message = "The firstName of the contact is required")
    private String firstName;

    private String companyName;

    private String businessOfficePhone;

    private String cellPhone;

    private String emailAddress;

    private String city;

    private String stateProvince;

    private String countryRegion;

    @NotBlank(message = "The title is required")
    private String title;

    private String pictureUrl;

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
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
