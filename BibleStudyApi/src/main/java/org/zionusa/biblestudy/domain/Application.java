package org.zionusa.biblestudy.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseApplication;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "applications")
public class Application extends BaseApplication {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "archived", columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    private String appleAppStoreUrl;

    private String description;

    private Boolean enabled = true;

    private String googlePlayStoreUrl;

    private String iconTitle;

    private String iconUrl;

    private String launchUrl;

    private String name;

    private String uniqueId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Application that = (Application) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
