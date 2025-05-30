package org.zionusa.admin.domain.activities;

import lombok.Data;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChurchActivityReport {
    List<ActivityCategoryReport> categories = new ArrayList<>();
    @Id
    private Integer id;
    private String city;
    private Integer maxActivityCount; // Sun (9), Mon (9), Tue (8), Wed (9), Thu (9), Fri(9), Sat (6) - special
    private String name;
    private Integer requiredActivityCount; // Sun (9), Mon (3), Tue (2), Wed (3), Thu (3), Fri(3), Sat (6)
    private Integer specialActivityCount;
    private String stateAbbrv;
    private Integer totalDays;
    private Integer worshipCount;
    private Integer typeId;
    private String type;

    public ChurchActivityReport() {
        this.maxActivityCount = 0;
        this.requiredActivityCount = 0;
        this.specialActivityCount = 0;
        this.totalDays = 0;
        this.worshipCount = 0;
    }
}
