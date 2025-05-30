package org.zionusa.preaching.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId1;

    private Integer userId2;

    private Integer userId3;

    private Integer preachingLogId;

    private String firstName;

    private String lastName;

    private String gender;

    private String phone;

    private String email;

    private String location;

    private String city;

    private Integer stateId;

    private String longitude;

    private String latitude;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String baptismDate;

    private String notes;

    private boolean archived = false;

    private Integer baptismChurchId;

    private Integer baptismLogId;

    private Date updatedDate;
}
