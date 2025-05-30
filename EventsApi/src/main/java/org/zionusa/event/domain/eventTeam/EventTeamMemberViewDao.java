package org.zionusa.event.domain.eventTeam;

import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface EventTeamMemberViewDao extends BaseDao<EventTeamMemberView, String> {
    EventTeamMemberView findByBranchId(Integer branchId);

    List<EventTeamMemberView>getAllEventTeamMemberViewByBranchIdAndApplicationRoleName(Integer id, String applicationRoleName);
    List<EventTeamMemberView> getAllEventTeamMemberViewByApplicationRoleName(String applicationRoleName);

}
