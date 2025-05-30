package org.zionusa.management.dao;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zionusa.management.domain.UserApplicationRole;

import java.util.List;
import java.util.Optional;

public interface UserApplicationRoleDao extends JpaRepository<UserApplicationRole, Integer> {

    List<UserApplicationRole> getAllByApplicationRoleName(String applicationRoleName);

    List<UserApplicationRole> findByUserId(Integer userId);

    Optional<UserApplicationRole> findByUserIdAndApplicationRoleId(Integer userId, Integer applicationRoleId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from UserApplicationRole where userId = :userId")
    void deleteUserApplicationRolesByUserId(@Param("userId") Integer userId);
}
