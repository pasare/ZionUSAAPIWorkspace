package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.User;

import java.util.List;

public interface UserApplicationRoleUserDao extends JpaRepository<User.ApplicationRole, String> {

    List<User.ApplicationRole> getAllByApplicationRoleId(Integer applicationRoleId);

    List<User.ApplicationRole> getAllByApplicationRoleName(String applicationRoleName);

    List<User.ApplicationRole> getApplicationRoleDisplayUserByChurchId(Integer churchId);


    List<User.ApplicationRole> getAllByChurchIdAndApplicationRoleName(Integer churchId, String applicationRoleName);

    List<User.ApplicationRole> getAllByAssociationIdAndApplicationRoleName(Integer associationId,
                                                                           String applicationRoleName);

    List<User.ApplicationRole> getAllByMainChurchIdAndApplicationRoleName(Integer mainChurchId,
                                                                            String applicationRoleName);

}
