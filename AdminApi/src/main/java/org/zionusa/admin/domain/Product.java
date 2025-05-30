package org.zionusa.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.domain.Auditable;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.enums.EBranchType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "products")
public class Product implements BaseDomain<Integer> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "productDetailsId", insertable = false, updatable = false)
    private ProductDetails productDetails;

    private String purchaseDate;

    @OneToOne
//    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "conditionId", insertable = false, updatable = false)
    private Condition currentCondition;

    private String warrantyDate;
    private String manufactureDate;
    private String serialNumber;
    private String purchasedLocation;
    private String barcode;
    private String location;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;


    @Data
    @Entity
    @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
    @Table(name = "product_categories")
    public static class Category implements BaseDomain<Integer> {

        @Id
        @EqualsAndHashCode.Include
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        private String name;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean archived = false;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean hidden = false;
    }

    @Data
    @Entity
    @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
    @Table(name = "product_conditions")
    public static class Condition implements BaseDomain<Integer> {

        @Id
        @EqualsAndHashCode.Include
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        private String name;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean archived = false;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean hidden = false;
    }

    @Data
    @Entity
    @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
    @Table(name = "product_details")
    public static class ProductDetails implements BaseDomain<Integer> {
        @Id
        @EqualsAndHashCode.Include
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        private String name;

        @OneToOne
//        @Enumerated(EnumType.STRING)
        @JoinColumn(name = "categoryId", insertable = false, updatable = false)
        private Category category;

        @Column(columnDefinition="TEXT")
        private String description;

        private String brand;
        private String model;
        private double price;

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "pictureId", insertable = false, updatable = false)
        @JsonIgnore
        private ProductPicture productPicture;
        @Transient
        private String pictureUrl;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean archived = false;

        @NotNull
        @Column(columnDefinition = "bit default 0")
        private Boolean hidden = false;
    }

    @Data
    @Entity
    @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
    @Table(name = "product_pictures")
    public static class ProductPicture implements BaseDomain<Integer> {

        @Id
        @EqualsAndHashCode.Include
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        private Integer productId;
        private String name;
        private String description;
        private String pictureUrlFull;
        private String pictureUrlMedium;
        private String thumbnailUrl;

        @Transient
        @JsonIgnore
        private byte[] pictureFull;

        @Transient
        @JsonIgnore
        private byte[] pictureMedium;

        @Transient
        @JsonIgnore
        private byte[] thumbnail;

    }
}
