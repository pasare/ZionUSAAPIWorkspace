package org.zionusa.preaching.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "PREACHING_LOG")
public class PreachingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId1;

    private Integer userId2;

    private Integer userId3;

    private int movementId;

    private int preached;

    private int contacts;

    private int baptisms;

    private int fruits;

    private int talents;

    private String location;

    private boolean active;

    private String longitude;

    private String latitude;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String date;

    private Date updatedDate;

    @Transient
    private Partner partner1;

    @Transient
    private Partner partner2;

    @Transient
    private Partner partner3;

}
