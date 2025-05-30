package org.zionusa.admin.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.constraints.DateFormatConstraint;
import org.zionusa.base.util.constraints.DateTimeFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "notifications")
public class Notification implements BaseDomain<Integer> {

    public static final String PREACHING_SOURCE = "preaching";
    public static final String EVENTS_SOURCE = "events";
    public static final String ACTIVITIES_SOURCE = "activities";
    public static final String BIBLE_STUDY_SOURCE = "bible-study";

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
    private Integer userId;

    private Integer parentChurchId;

    @NotNull
    private Integer churchId;

    private Integer groupId;

    private Integer teamId;

    @NotNull
    private String userName;

    @NotNull
    private String churchName;

    @NotEmpty
    private String source;

    @NotEmpty
    private String subSource;

    @NotEmpty
    private String title;

    @NotEmpty
    private String subtitle;

    @NotEmpty
    private String content;

    private String metadata;

    @NotNull
    private Integer objectId;

    @NotNull
    private boolean processed = false;

    private Date processedDate;

    private boolean ignoreEnvironment = false;

    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;

    @DateTimeFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String notificationDateTime;

    private Date createdDate;

    private Date updatedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
