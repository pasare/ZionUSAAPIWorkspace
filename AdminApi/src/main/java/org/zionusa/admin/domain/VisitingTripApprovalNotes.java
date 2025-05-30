package org.zionusa.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitingTripApprovalNotes {
    private Integer visitingTripId;
    private String notes;
}
