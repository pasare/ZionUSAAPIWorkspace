package org.zionusa.biblestudy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.zionusa.base.domain.BaseDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "sermon_recordings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SermonRecording implements BaseDomain<Integer> {

    public static final String SERMON_UPLOADED_SUB_SOURCE = "sermon-recording-uploaded";
    public static final String SERMON_GRADED_SUB_SOURCE = "sermon-recording-graded";
    public static final String SERMON_EXAMPLE_SUB_SOURCE = "sermon-recording-example";

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

    private Integer preacherId;

    private Integer graderId;

    private Integer parentChurchId;

    private Integer churchId;

    private Integer groupId;

    private Integer teamId;

    @Column(name = "study_id")
    private Integer studyId;

    private String preacherName;

    private String churchName;

    //@DateFormatConstraint
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;

    private String duration;

    private Integer grade;

    private String signatureType;

    private boolean pastorsSignature;

    private boolean exampleSermon;

    private String audioUrl;

    private Integer attemptNumber;

    private String comments;

    private Date createdDate;

    private Date updatedDate;

    @Transient
    private String metadata;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "study_id", insertable = false, updatable = false)
    private Study study;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SermonRecording that = (SermonRecording) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
