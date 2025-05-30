package org.zionusa.event.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventApprover {
    @EqualsAndHashCode.Include
    private Integer id;
    private String email;
    private String name;
}
