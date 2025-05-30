package org.zionusa.event.domain.eventMarketing;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.event.domain.EventMarketingMaterial;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Audited
@AllArgsConstructor
@Table(name = "event_marketing")
public class EventMarketing implements BaseDomain<Integer> {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "eventMarketingId", nullable = false)
    @ToString.Exclude
    List<EventMarketingMaterial> eventMarketingMaterials;
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
    private String additionalBannerDetails;
    private Boolean advertising;
    private Boolean advertisingWebsite;
    private String advertisingWhyNot;
    private Boolean allowFundraising;
    private String bannerMount;
    private Integer banners;
    private Boolean flyerAssistance;
    private String invitationStrategy;
    private Boolean makingFlyer;
    private String materialsOptions;
    private String followUpEventName;
    private String followUpEventDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventMarketing that = (EventMarketing) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
