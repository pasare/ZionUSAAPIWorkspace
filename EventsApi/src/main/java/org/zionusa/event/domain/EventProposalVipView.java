package org.zionusa.event.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "event_proposals_vips_view")
public class EventProposalVipView implements BaseDomain<Integer> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean archived = false;
    private Boolean hidden = false;
    private Integer vipId;
    private String businessOfficePhone;
    private String cellPhone;
    private String city;
    private String companyName;
    private String countryRegion;
    private String emailAddress;
    private String facebookUrl;
    private Integer eventCategoryId;
    private String eventCategoryTitle;
    private Integer eventProposalId;
    private String eventProposalBranchId;
    private String eventProposalBranchName;
    private String eventProposalProposedDate;
    private String eventProposalTitle;
    private Integer eventTypeId;
    private String eventTypeTitle;
    private String firstName;
    private String instagramUrl;
    private String lastName;
    private String linkedInUrl;
    private String pictureUrl;
    private String stateProvince;
    private String title;
    private String twitterUrl;
//    private String instances

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventProposalVipView that = (EventProposalVipView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
