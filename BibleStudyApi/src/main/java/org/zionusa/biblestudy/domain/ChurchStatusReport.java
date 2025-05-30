package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.zionusa.base.util.constraints.DateFormatConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "church_status_reports", uniqueConstraints = { @UniqueConstraint(columnNames = {"churchId", "date"}) })
public class ChurchStatusReport {

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @DateFormatConstraint
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;

    @NotNull
    private Integer churchId;

    @NotNull
    private String churchName;

    private String churchType;

    private String parentChurchId;

    private Integer attendanceCount;

    // Signatures

    private Integer memberGaSignatureCount;

    @Column(name = "member_r_signature_count")
    private Integer memberRSignatureCount;

    private Integer memberRtSignatureCount;

    @Column(name = "member_t_signature_count")
    private Integer memberTSignatureCount;

    // Preaching

    private Integer contactsConnectedCount;

    private Integer bibleStudyCount;

    private Integer awaitingBaptismCount;

    private Integer baptismCount;

    // Leader

    @Column(name = "leader_t_signature_count")
    private Integer leaderTSignatureCount;

    private Integer leaderBibleStudyCount;

    private Integer leaderGaSignaturesGivenCount;

    @Column(name = "leader_r_signatures_given_count")
    private Integer leaderRSignaturesGivenCount;
}
