package org.zionusa.biblestudy.domain;

import lombok.Data;
import org.zionusa.base.domain.Member;

@Data
public class MemberGoalReport {

    Integer fruit = 0;

    Member member;

    Integer signatures = 0;

}
