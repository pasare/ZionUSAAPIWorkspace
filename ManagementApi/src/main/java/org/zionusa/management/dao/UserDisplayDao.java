package org.zionusa.management.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zionusa.management.domain.User;
import org.zionusa.management.domain.User.DisplayUser;

import java.util.List;

public interface UserDisplayDao extends JpaRepository<User.DisplayUser, Integer> {

    Integer countAllByEnabledIsTrue();

    List<DisplayUser> getAllByRoleId(Integer roleId);

    List<DisplayUser> getAllByAccessId(Integer accessId);

    List<DisplayUser> getAllByAssociationId(Integer associationId);

    List<DisplayUser> getAllByAssociationIdAndEnabledIsTrue(Integer associationId);

    List<DisplayUser> getAllByAssociationIdAndTeacherTrue(Integer associationId);

    List<DisplayUser> getAllByParentChurchId(Integer parentChurchId);

    List<DisplayUser> getAllByChurchId(Integer churchId);

    List<DisplayUser> getAllByChurchIdAndAccessId(Integer churchId, Integer accessId);

    List<DisplayUser> getAllByGroupId(Integer groupId);

    List<DisplayUser> getAllByTeamId(Integer teamId);

    List<DisplayUser> getAllByGaGraderTrue();

    List<DisplayUser> getAllByReadyGraderTrue();

    List<DisplayUser> getAllByReadyGraderTrueAndMainChurchId(Integer churchId);

    List<DisplayUser> getAllByTeacherTrue();

    List<DisplayUser> getAllByTeacherTrueAndChurchId(Integer churchId);

    List<DisplayUser> getAllByEnabledTrueAndChurchIdNot(Integer churchId);

    List<DisplayUser> getAllByRoleIdOrGaGraderTrueAndMainChurchId(Integer roleId, Integer churchId);

    List<DisplayUser> getAllByDisplayNameContainsOrUsernameContainsOrChurchNameContains(
            String displayName,
            String username,
            String churchName
    );
}
