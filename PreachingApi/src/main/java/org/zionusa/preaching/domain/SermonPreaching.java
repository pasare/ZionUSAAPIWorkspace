package org.zionusa.preaching.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "sermon_preaching")
public class SermonPreaching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private Integer bookNumber;

    private Integer chapterNumber;

    private Integer sortOrder;

    private String imageUrl;

    private String themeUrl;

    private Boolean visible;

    private String languageCode;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sermonPreachingId", nullable = false, insertable = false, updatable = false)
    private List<SermonPreachingVerse> verses;
}
