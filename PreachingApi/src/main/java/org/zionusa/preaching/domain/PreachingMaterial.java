package org.zionusa.preaching.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "preaching_materials")
public class PreachingMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="title", columnDefinition = "NVARCHAR")
    private String title;

    @Column(name= "category", columnDefinition = "NVARCHAR")
    private String category;

    @Column(name = "subject", columnDefinition = "NVARCHAR")
    private String subject;

    private Integer categoryId;

    private Integer subjectId;

    @Column(name = "materialUrl", columnDefinition = "NVARCHAR")
    private String materialUrl;

    private Integer sortOrder;

    @Column(name = "imageUrl", columnDefinition = "NVARCHAR")
    private String imageUrl;

    private String themeUrl;

    private Boolean visible;

    private String languageCode;

    private Boolean shareable;
}
