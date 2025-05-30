package org.zionusa.admin.domain.movement;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.admin.domain.movementactivity.MovementActivity;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "movements")
public class Movement implements BaseDomain<Integer> {

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
    private String name;

    @NotNull
    private boolean active;

    private String mobileBannerUrl;

    private String webBannerUrl;

    @Transient
    private Integer participantCount;

    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String startDate;

    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String endDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "movementId")
    private List<MovementActivity> movementActivities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Movement movement = (Movement) o;
        return Objects.equals(id, movement.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Data
    @Entity
    @Table(name = "movements_group_baptisms_leader_input", uniqueConstraints = {@UniqueConstraint(columnNames = {
        "movementId", "branchId",  "groupId", "date"})})
    public static class GroupBaptismsLeaderInput implements BaseDomain<Integer> {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean archived = false;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean hidden = false;

        private Integer movementId;

        @NotNull
        private Integer branchId;

        @NotNull
        private Integer groupId;

        @NotNull
        private String date;

        @NotNull
        private Integer baptisms;

        @NotNull
        private String branchName;

        @NotNull
        private String groupName;
    }

    @Data
    @Entity
    @Table(name = "movements_branch_baptisms_leader_input", uniqueConstraints = {@UniqueConstraint(columnNames = {"movementId", "branchId", "date"})})
    public static class BranchBaptismsLeaderInput implements BaseDomain<Integer> {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean archived = false;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean hidden = false;

        private Integer movementId;

        @NotNull
        private Integer branchId;

        @NotNull
        private String date;

        @NotNull
        private Integer baptisms;

        @Transient
        private String branchName;
        @Transient
        private Integer mainBranchId;
        @Transient
        private String mainBranchName;
    }

    @Data
    @Entity
    @Table(name = "movements_main_branch_baptisms_leader_input", uniqueConstraints = {@UniqueConstraint(columnNames = {"movementId", "branchId", "date"})})
    public static class MainBranchBaptismsLeaderInput implements BaseDomain<Integer> {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean archived = false;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean hidden = false;

        private Integer movementId;

        @NotNull
        private Integer branchId;

        @NotNull
        private String date;

        @NotNull
        private Integer baptisms;
    }

    @Data
    @Entity
    @Immutable
    @Table(name = "movements_branch_baptisms_leader_input_view")
    public static class BranchBaptismsLeaderInputView implements BaseDomain<Integer> {
        @Transient
        private Integer id;
        private Boolean archived = false;
        private Boolean hidden = false;

        @Id
        private Integer branchId;
        private String branchName;
        private Integer movementId;
        private Integer associationId;
        private Integer baptisms;
    }

    @Data
    @Entity
    @Immutable
    @Table(name = "movements_main_branch_baptisms_leader_input_view")
    public static class MainBranchBaptismsLeaderInputView implements BaseDomain<Integer> {
        @Transient
        private Integer id;
        private Boolean archived = false;
        private Boolean hidden = false;

        @Id
        private Integer branchId;
        private String branchName;
        private Integer movementId;
        private Integer associationId;
        private Integer baptisms;
    }
}
