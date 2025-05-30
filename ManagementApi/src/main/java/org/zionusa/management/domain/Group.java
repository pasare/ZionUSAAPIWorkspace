package org.zionusa.management.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "GROUPS")
public class Group implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean hidden = false;

    @NotNull
    private Integer churchId;

    private Integer leaderId;

    private String name;

    @NotNull
    private boolean churchGroup;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leaderId", insertable = false, updatable = false)
    private User leader;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "churchId", insertable = false, updatable = false)
    @ToString.Exclude
    private Church church;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    @ToString.Exclude
    private List<Team> teams = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "group_display_view")
    public static class DisplayGroup {

        @Id
        private Integer id;
        private Integer churchId;
        private Integer leaderId;
        private String leaderName;
        private String name;
        private boolean churchGroup;
        private Boolean archived = false;

        @OneToMany(fetch = FetchType.EAGER)
        @JoinColumn(name = "groupId", insertable = false, updatable = false)
        private List<Team.DisplayTeam> teams;
    }
}
