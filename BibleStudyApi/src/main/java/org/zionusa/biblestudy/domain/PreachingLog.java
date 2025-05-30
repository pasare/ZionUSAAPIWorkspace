package org.zionusa.biblestudy.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Immutable;
import org.hibernate.envers.Audited;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;
import org.zionusa.base.util.constraints.TimeFormatConstraint;

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
@Table(name = "preaching_logs")
public class PreachingLog implements BaseDomain<Integer> {
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

    @NotNull
    @Column(columnDefinition = "int default 0")
    private Integer acquaintances;

    private Integer contacts;

    @Column(columnDefinition = "int default 0")
    private Integer coWorkers;

    @NotNull
    @DateFormatConstraint()
    private String date;

    @TimeFormatConstraint()
    private String endTime;

    @Column(columnDefinition = "int default 0")
    private Integer family;

    @Column(columnDefinition = "int default 0")
    private Integer friends;

    @Column(columnDefinition = "int default 0")
    private Integer fruit; // 1000 points

    private String latitude;

    private String location;

    private String longitude;

    @Column(columnDefinition = "int default 0")
    private Integer meaningful; // 10 points

    private Integer movementId;

    @Column(columnDefinition = "int default 0")
    private Integer neighbors;

    @Column(columnDefinition = "int default 0")
    private Integer simple; // 1 point

    private Boolean submitted; // 50 points

    // New APA Movement (Preaching)
    @Column(columnDefinition = "int default 0")
    private Integer bibleStudy; // 100 points

    @Column(columnDefinition = "int default 0")
    private Integer lostSheepKeepService; // 1000 points

    @Column(columnDefinition = "int default 0")
    private Integer feedMySheep; // 25 points

    @Column(columnDefinition = "int default 0")
    private Integer fruitAttendance; // 1000 points

    // New APA Movement (Teaching)
    @Column(columnDefinition = "int default 0")
    private Integer practiceSignature; // 1 point

    @Column(columnDefinition = "int default 0")
    private Integer gaSignature; // 10 points

    @Column(columnDefinition = "int default 0")
    private Integer rSignature; // 50 points

    @Column(columnDefinition = "int default 0")
    private Integer rtSignature; // 100 points

    @Column(columnDefinition = "int default 0")
    private Integer tSignature; // 500 points

    @Column(columnDefinition = "int default 0")
    private Integer examplePreacher; // 1000 points

    // New APA Movement (Activities)
    @Column(columnDefinition = "int default 0")
    private Integer prayDailyTogether; // 5 points

    @Column(columnDefinition = "int default 0")
    private Integer completeMyPage; // 50 points

    @Column(columnDefinition = "int default 0")
    private Integer levelUpOnEduLms; // 100 points

    @Column(columnDefinition = "int default 0")
    private Integer writeLetterToMother; // 150 points

    @Column(columnDefinition = "int default 0")
    private Integer joinShortTermPreaching; // 500 points

    @Column(columnDefinition = "int default 0")
    private Integer prayForAssociation; // 500 points

    @NotNull
    @TimeFormatConstraint()
    private String startTime;

    @NotNull
    private Integer userId1;

    private String userDisplayName1;

    private Integer userId2;

    private String userDisplayName2;

    private Integer userId3;

    private String userDisplayName3;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PreachingLog that = (PreachingLog) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public boolean hasPreaching() {
        // New APA Movement (Teaching)
        return greaterThanZero(meaningful) ||
            greaterThanZero(simple) ||
            greaterThanZero(feedMySheep) ||
            greaterThanZero(bibleStudy) ||
            greaterThanZero(fruit) ||
            greaterThanZero(lostSheepKeepService) ||
            greaterThanZero(fruitAttendance) ||

            // New APA Movement (Teaching)
            greaterThanZero(practiceSignature) ||
            greaterThanZero(gaSignature) ||
            greaterThanZero(rSignature) ||
            greaterThanZero(rtSignature) ||
            greaterThanZero(tSignature) ||
            greaterThanZero(examplePreacher) ||

            // New APA Movement (Activities)
            greaterThanZero(prayDailyTogether) ||
            greaterThanZero(completeMyPage) ||
            greaterThanZero(levelUpOnEduLms) ||
            greaterThanZero(writeLetterToMother) ||
            greaterThanZero(joinShortTermPreaching) ||
            greaterThanZero(prayForAssociation) ||

            greaterThanZero(acquaintances) ||
            greaterThanZero(contacts) ||
            greaterThanZero(coWorkers) ||
            greaterThanZero(family) ||
            greaterThanZero(friends) ||
            greaterThanZero(neighbors);
    }

    private boolean greaterThanZero(Integer count) {
        return count != null && count > 0;
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "preaching_log_association_daily_view")
    public static class Association {

        @Id
        @EqualsAndHashCode.Include
        private String uniqueId;

        private Integer id;

        private String date;

        private String name;

        // Preaching

        private Integer acquaintances = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer meaningful = 0;

        private Integer neighbors = 0;

        private Integer simple = 0;

        private Integer numLogs = 0;

        public static Association add(Association a, Association b) {
            Association c = new Association();
            c.setId(b.getId());
            // Ignore date
            c.setName(b.getName());

            // Preaching
            c.setAcquaintances(a.getAcquaintances() + b.getAcquaintances());
            c.setContacts(a.getContacts() + b.getContacts());
            c.setCoWorkers(a.getCoWorkers() + b.getCoWorkers());
            c.setFamily(a.getFamily() + b.getFamily());
            c.setFriends(a.getFriends() + b.getFriends());
            c.setFruit(a.getFruit() + b.getFruit());
            c.setMeaningful(a.getMeaningful() + b.getMeaningful());
            c.setNeighbors(a.getNeighbors() + b.getNeighbors());
            c.setSimple(a.getSimple() + b.getSimple());
            c.setNumLogs(a.getNumLogs() + b.getNumLogs());
            return c;
        }

    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "preaching_log_church_daily_view")
    public static class Church {

        @Id
        @EqualsAndHashCode.Include
        private String uniqueId;

        private Integer associationId;

        private Integer mainChurchId;

        private Integer regionId;

        private String displayName;

        private String regionName;

        private Integer id;

        private String city;

        private String date;

        private String name;

        private String stateAbbrv;

        private String stateName;

        // Preaching

        private Integer acquaintances = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer meaningful = 0;

        private Integer neighbors = 0;

        private Integer simple = 0;

        private Integer numLogs = 0;

        public static Church add(Church a, Church b) {
            Church c = new Church();
            c.setAssociationId(b.getAssociationId());
            c.setId(b.getId());
            c.setRegionName(b.getRegionName());
            c.setRegionId(b.getRegionId());
            c.setDisplayName(b.getDisplayName());
            // Ignore date
            c.setMainChurchId(b.getMainChurchId());
            c.setCity(b.getCity());
            c.setName(b.getName());
            c.setStateAbbrv(b.getStateAbbrv());
            c.setStateName(b.getStateName());

            // Preaching
            c.setAcquaintances(a.getAcquaintances() + b.getAcquaintances());
            c.setContacts(a.getContacts() + b.getContacts());
            c.setCoWorkers(a.getCoWorkers() + b.getCoWorkers());
            c.setFamily(a.getFamily() + b.getFamily());
            c.setFriends(a.getFriends() + b.getFriends());
            c.setFruit(a.getFruit() + b.getFruit());
            c.setMeaningful(a.getMeaningful() + b.getMeaningful());
            c.setNeighbors(a.getNeighbors() + b.getNeighbors());
            c.setSimple(a.getSimple() + b.getSimple());
            c.setNumLogs(a.getNumLogs() + b.getNumLogs());
            return c;
        }
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "preaching_log_region_daily_view")
    public static class Region {

        @Id
        @EqualsAndHashCode.Include
        private String uniqueId;

        private Integer regionId = 0;

        private String date;

        private String regionName;

        private String displayName;

        // Preaching

        private Integer acquaintances = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer meaningful = 0;

        private Integer neighbors = 0;

        private Integer simple = 0;

        private Integer numLogs = 0;

        public static Region add(RegionWithGoals a, Region b) {
            Region c = new Region();
            c.setUniqueId(b.getUniqueId());
            c.setRegionId(b.getRegionId());
            c.setDisplayName(b.getDisplayName());
            // Ignore date
            c.setRegionName(b.getRegionName());


            // Preaching
            c.setAcquaintances(a.getAcquaintances() + b.getAcquaintances());
            c.setContacts(a.getContacts() + b.getContacts());
            c.setCoWorkers(a.getCoWorkers() + b.getCoWorkers());
            c.setFamily(a.getFamily() + b.getFamily());
            c.setFriends(a.getFriends() + b.getFriends());
            c.setFruit(a.getFruit() + b.getFruit());
            c.setMeaningful(a.getMeaningful() + b.getMeaningful());
            c.setNeighbors(a.getNeighbors() + b.getNeighbors());
            c.setSimple(a.getSimple() + b.getSimple());
            c.setNumLogs(a.getNumLogs() + b.getNumLogs());
            return c;
        }

        public static RegionWithGoals convertToRegionWithGoals(Region b) {
            RegionWithGoals c = new RegionWithGoals();
            c.setUniqueId(b.getUniqueId());
            c.setRegionId(b.getRegionId());
            // Ignore date
            c.setRegionName(b.getRegionName());
            c.setDisplayName(c.getDisplayName());

            // Preaching
            c.setAcquaintances(b.getAcquaintances());
            c.setContacts(b.getContacts());
            c.setCoWorkers(b.getCoWorkers());
            c.setFamily(b.getFamily());
            c.setFriends(b.getFriends());
            c.setFruit(b.getFruit());
            c.setMeaningful(b.getMeaningful());
            c.setNeighbors(b.getNeighbors());
            c.setSimple(b.getSimple());
            c.setNumLogs(b.getNumLogs());

            return c;
        }
    }

    @Data
    public static class RegionWithGoals {

        @Id
        @EqualsAndHashCode.Include
        private String uniqueId;

        private Integer regionId;

        private String date;

        private String displayName;

        private String regionName;

        // Preaching

        private Integer acquaintances = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer meaningful = 0;

        private Integer neighbors = 0;

        private Integer simple = 0;

        private Integer numLogs = 0;

        private Integer simpleGoal = 0;

        private double simplePercentage = 0;

        private Integer meaningfulGoal = 0;

        private double meaningfulPercentage = 0;

        private Integer fruitGoal = 0;

        private double fruitPercentage = 0;

        private Integer rank = -1;

        public static RegionWithGoals add(RegionWithGoals a, Region b) {
            RegionWithGoals c = new RegionWithGoals();
            c.setUniqueId(b.getUniqueId());
            c.setRegionId(b.getRegionId());
            // Ignore date
            c.setRegionName(b.getRegionName());
            c.setDisplayName(b.getDisplayName());
            c.setSimpleGoal(a.getSimpleGoal());
            c.setMeaningfulGoal(a.getMeaningfulGoal());
            c.setFruitGoal(a.getFruitGoal());


            // Preaching
            c.setAcquaintances(a.getAcquaintances() + b.getAcquaintances());
            c.setContacts(a.getContacts() + b.getContacts());
            c.setCoWorkers(a.getCoWorkers() + b.getCoWorkers());
            c.setFamily(a.getFamily() + b.getFamily());
            c.setFriends(a.getFriends() + b.getFriends());
            c.setFruit(a.getFruit() + b.getFruit());
            c.setMeaningful(a.getMeaningful() + b.getMeaningful());
            c.setNeighbors(a.getNeighbors() + b.getNeighbors());
            c.setSimple(a.getSimple() + b.getSimple());
            c.setNumLogs(a.getNumLogs() + b.getNumLogs());
            return c;
        }
    }

    @Data
    public static class ChurchWithGoals {

        @Id
        @EqualsAndHashCode.Include
        private String uniqueId;

        private Integer associationId;

        private Integer mainChurchId;

        private String thumbnailUrl;

        private String pictureUrlMedium;

        private Integer regionId;

        private String displayName;

        private String regionName;

        private Integer id;

        private String city;

        private String date;

        private String name;

        private String stateAbbrv;

        private String stateName;

        // Preaching

        private Integer acquaintances = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer meaningful = 0;

        private Integer neighbors = 0;

        private Integer simple = 0;

        private Integer numLogs = 0;

        private Integer simpleGoal = 0;

        private double simplePercentage = 0;

        private Integer meaningfulGoal = 0;

        private double meaningfulPercentage = 0;

        private Integer fruitGoal = 0;

        private double fruitPercentage = 0;

        private Integer rank = -1;

        public static ChurchWithGoals add(ChurchWithGoals a, Church b) {
            ChurchWithGoals c = new ChurchWithGoals();
            c.setAssociationId(b.getAssociationId());
            c.setId(b.getId());
            // Ignore date
            c.setMainChurchId(b.getMainChurchId());
            c.setCity(b.getCity());
            c.setName(b.getName());
            c.setStateAbbrv(b.getStateAbbrv());
            c.setStateName(b.getStateName());

            c.setRegionId(b.getRegionId());
            c.setRegionName(b.getRegionName());
            c.setDisplayName(b.getDisplayName());

            c.setSimpleGoal(a.getSimpleGoal());
            c.setMeaningfulGoal(a.getMeaningfulGoal());
            c.setFruitGoal(a.getFruitGoal());

            // Preaching
            c.setAcquaintances(a.getAcquaintances() + b.getAcquaintances());
            c.setContacts(a.getContacts() + b.getContacts());
            c.setCoWorkers(a.getCoWorkers() + b.getCoWorkers());
            c.setFamily(a.getFamily() + b.getFamily());
            c.setFriends(a.getFriends() + b.getFriends());
            c.setFruit(a.getFruit() + b.getFruit());
            c.setMeaningful(a.getMeaningful() + b.getMeaningful());
            c.setNeighbors(a.getNeighbors() + b.getNeighbors());
            c.setSimple(a.getSimple() + b.getSimple());
            c.setNumLogs(a.getNumLogs() + b.getNumLogs());
            return c;
        }

        public static ChurchWithGoals convertToChurchWithGoals(Church b) {
            ChurchWithGoals c = new ChurchWithGoals();
            c.setAssociationId(b.getAssociationId());
            c.setId(b.getId());
            // Ignore date
            c.setMainChurchId(b.getMainChurchId());
            c.setCity(b.getCity());
            c.setName(b.getName());
            c.setStateAbbrv(b.getStateAbbrv());
            c.setStateName(b.getStateName());

            c.setRegionId(b.getRegionId());
            c.setRegionName(b.getRegionName());
            c.setDisplayName(b.getDisplayName());

            // Preaching
            c.setAcquaintances(b.getAcquaintances());
            c.setContacts(b.getContacts());
            c.setCoWorkers(b.getCoWorkers());
            c.setFamily(b.getFamily());
            c.setFriends(b.getFriends());
            c.setFruit(b.getFruit());
            c.setMeaningful(b.getMeaningful());
            c.setNeighbors(b.getNeighbors());
            c.setSimple(b.getSimple());
            c.setNumLogs(b.getNumLogs());

            return c;
        }
    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Immutable
    @Table(name = "preaching_log_overseer_daily_view")
    public static class Overseer {

        @Id
        @EqualsAndHashCode.Include
        private String uniqueId;

        private Integer associationId;

        private Integer id;

        private String city;

        private String thumbnailUrl;

        private String pictureUrlMedium;

        private String date;

        private String name;

        private String stateAbbrv;

        private String stateName;

        // Preaching

        private Integer acquaintances = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer meaningful = 0;

        private Integer neighbors = 0;

        private Integer simple = 0;

        private Integer numLogs = 0;

        public static Overseer add(Overseer a, Overseer b) {
            Overseer c = new Overseer();
            c.setAssociationId(b.getAssociationId());
            c.setId(b.getId());
            // Ignore date
            c.setCity(b.getCity());
            c.setThumbnailUrl(b.getThumbnailUrl());
            c.setPictureUrlMedium(b.getPictureUrlMedium());
            c.setName(b.getName());
            c.setStateAbbrv(b.getStateAbbrv());
            c.setStateName(b.getStateName());

            // Preaching
            c.setAcquaintances(a.getAcquaintances() + b.getAcquaintances());
            c.setContacts(a.getContacts() + b.getContacts());
            c.setCoWorkers(a.getCoWorkers() + b.getCoWorkers());
            c.setFamily(a.getFamily() + b.getFamily());
            c.setFriends(a.getFriends() + b.getFriends());
            c.setFruit(a.getFruit() + b.getFruit());
            c.setMeaningful(a.getMeaningful() + b.getMeaningful());
            c.setNeighbors(a.getNeighbors() + b.getNeighbors());
            c.setSimple(a.getSimple() + b.getSimple());
            c.setNumLogs(a.getNumLogs() + b.getNumLogs());
            return c;
        }

    }

    @Data
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Table(name = "preaching_log_all_daily_view")
    public static class PreachingLogTotals {

        @Transient
        private Integer id;

        @Id
        @EqualsAndHashCode.Include
        private String date;

        @Transient
        private String name;

        // Preaching

        private Integer acquaintances = 0;

        private Integer contacts = 0;

        private Integer coWorkers = 0;

        private Integer family = 0;

        private Integer friends = 0;

        private Integer fruit = 0;

        private Integer meaningful = 0;

        private Integer neighbors = 0;

        private Integer simple = 0;

        // Metadata

        private Integer numLogs = 0;

        @Transient
        private Integer goal = 0;

        @Transient
        private Integer members = 0;

    }

    @Data
    public static class RegionWithChurches {

        public List<ChurchWithGoals> churches;
        @Id
        private String uniqueId;
        private Integer regionId = 0;
        private String displayName;

        // Preaching
        private String regionName;
        private Integer acquaintances = 0;
        private Integer contacts = 0;
        private Integer coWorkers = 0;
        private Integer family = 0;
        private Integer friends = 0;
        private Integer fruit = 0;
        private Integer fruitGoal = 0;
        private Integer meaningful = 0;
        private Integer meaningfulGoal = 0;
        private Integer neighbors = 0;
        private Integer simple = 0;
        private Integer simpleGoal = 0;
        private Integer numLogs = 0;
        private Integer rank = -1;

    }
}
