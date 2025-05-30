package org.zionusa.event.domain.eventTeam;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.eventTeam.EventTeam;

public interface EventTeamsDao extends BaseDao<EventTeam, Integer> {
    EventTeam findByBranchId(Integer branchId);
}
