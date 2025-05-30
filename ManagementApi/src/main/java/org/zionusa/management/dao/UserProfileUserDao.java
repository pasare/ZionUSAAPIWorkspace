package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.User;

import java.util.List;

public interface UserProfileUserDao extends JpaRepository<User.ProfileUser, Integer> {

    User.ProfileUser findProfileUserById(Integer userId);

    List<User.ProfileUser> findProfileUsersByAssociationId(Integer associationId);

    List<User.ProfileUser> findProfileUsersByChurchId(Integer churchId);

    List<User.ProfileUser> findUserProfilesByGroupId(Integer groupId);

    List<User.ProfileUser> findUserProfilesByTeamId(Integer teamId);
}
