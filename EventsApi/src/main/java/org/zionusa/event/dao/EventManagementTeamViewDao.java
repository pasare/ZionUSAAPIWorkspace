package org.zionusa.event.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.EventManagementTeamView;

import java.util.List;

public interface EventManagementTeamViewDao extends BaseDao<EventManagementTeamView, String> {
    List<EventManagementTeamView> getEventManagementTeamViewByBranchIdAndApplicationRoleName(Integer branchId, String applicationRoleName);

    List<EventManagementTeamView> getEventManagementTeamViewsByBranchIdOrApplicationRoleNameContainingOrApplicationRoleNameOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContaining
        (Integer branchId, String applicationRoleName, String applicationRoleName2, String applicationRoleName3, String applicationRoleName4, String applicationRoleName5, String applicationRoleName6, String applicationRoleName7, String applicationRoleName8, String applicationRoleName9);

    List<EventManagementTeamView> getEventManagementTeamViewsByApplicationRoleNameContainingOrApplicationRoleNameOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContainingOrApplicationRoleNameContaining
        (String applicationRoleName, String applicationRoleName2, String applicationRoleName3, String applicationRoleName4, String applicationRoleName5, String applicationRoleName6, String applicationRoleName7, String applicationRoleName8, String applicationRoleName9);

    List<EventManagementTeamView> getEventManagementTeamViewByBranchId(Integer branchId);
    List<EventManagementTeamView> getEventManagementTeamViewByBranchIdAndApplicationRoleNameContaining(Integer branchId, String applicationName);

    List<EventManagementTeamView> getEventManagementTeamViewByApplicationRoleName(String applicationRoleName);

    List<EventManagementTeamView> getEventManagementTeamViewByUserId(Integer userId);

}
