package org.zionusa.biblestudy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReformerResult {

    @EqualsAndHashCode.Include
    private Integer id;
    private String displayName;
    private String church;
    private String reformerLevel;
    private String startDate;
    private String endDate;

    public ReformerResult(Reformer reformer) {
        this.id = reformer.getId();
        this.displayName = reformer.getName();
        this.church = reformer.getChurchName();
        //this.reformerLevel = reformer.getReformerType().getName();
        this.startDate = reformer.getStartDate();
        this.endDate = reformer.getEndDate();
    }
}
