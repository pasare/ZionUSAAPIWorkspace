package org.zionusa.preaching.domain;


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sermon_preaching_verses")
public class SermonPreachingVerse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer sermonPreachingId;

    private String book;

    private String chapter;

    private String verse;

    @Type(type="text")
    private String verseText;

    private String explanation;

    private String imageUrl;

    private Integer sortOrder;
}
