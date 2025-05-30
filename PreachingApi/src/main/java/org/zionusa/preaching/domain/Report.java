package org.zionusa.preaching.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "reports")
public class Report {

    public Report() {
        preachedSession = 0;
        contactsSession = 0;
        baptismsSession = 0;
        fruitsSession = 0;
        talentsSession = 0;
        preachedIndividual = 0;
        contactsIndividual = 0;
        baptismsIndividual = 0;
        fruitsIndividual = 0;
        talentsIndividual = 0;
    }

    public Report(Report report) {
        this.id = report.getId();
        this.userId = report.getUserId();
        this.teamId = report.getTeamId();
        this.groupId = report.getGroupId();
        this.churchId = report.getChurchId();
        this.parentChurchId = report.getParentChurchId();
        this.associationId = report.getAssociationId();
        this.date = report.getDate();
        this.movementId = report.getMovementId();
        this.createdDate = report.getCreatedDate();
        this.updatedDate = report.getUpdatedDate();

        preachedSession = 0;
        contactsSession = 0;
        baptismsSession = 0;
        fruitsSession = 0;
        talentsSession = 0;
        preachedIndividual = 0;
        contactsIndividual = 0;
        baptismsIndividual = 0;
        fruitsIndividual = 0;
        talentsIndividual = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer movementId;

    private Integer userId;

    private Integer teamId;

    private Integer groupId;

    private Integer churchId;

    private Integer parentChurchId;

    private Integer associationId;

    private int preachedSession;

    private int contactsSession;

    private int baptismsSession;

    private int fruitsSession;

    private int talentsSession;

    private double preachedIndividual;

    private double contactsIndividual;

    private double baptismsIndividual;

    private double fruitsIndividual;

    private double talentsIndividual;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String date;

    private Date createdDate;

    private Date updatedDate;
}
