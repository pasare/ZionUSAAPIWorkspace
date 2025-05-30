package org.zionusa.management.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "TEAMS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team implements BaseDomain<Integer> {

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
    private Integer groupId;

    private Integer leaderId;

    private String name;

    @NotNull
    private boolean churchTeam;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leaderId", insertable = false, updatable = false)
    private User leader;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    @ToString.Exclude
    private Group group;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId", insertable = false, updatable = false)
    @ToString.Exclude
    private List<User> members = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "teams_church_team_view")
    public static class ChurchTeam {

        @Id
        @EqualsAndHashCode.Include
        private Integer id;
        private Integer associationId;
        private String associationName;
        private String churchCity;
        private Integer churchId;
        private String churchName;
        private String churchStateFullName;
        private Integer churchStateId;
        private String churchStateShortName;
        private Integer groupId;
        private String groupName;
        private Integer mainChurchId;
        private String name;
        private Integer parentChurchId;
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "team_display_view")
    public static class DisplayTeam {

        @Id
        @EqualsAndHashCode.Include
        private Integer id;
        private Integer groupId;
        private Integer leaderId;
        private String leaderName;
        private String name;
        private boolean churchTeam;
        private Boolean archived = false;

        @OneToMany(fetch = FetchType.EAGER)
        @JoinColumn(name = "teamId", insertable = false, updatable = false)
        private List<User.DisplayUser> members = new ArrayList<>();
    }
}
