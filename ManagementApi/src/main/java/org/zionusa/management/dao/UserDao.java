package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zionusa.base.dao.BaseDao;
import org.zionusa.management.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends BaseDao<User, Integer> {

    List<User> getByTeamId(Integer teamId);

    List<User> getUsersByGenderOrderByFirstNameAsc(String gender);

    Optional<User> getUserByUsernameIgnoreCase(String username);

    Optional<User> getUserByActiveDirectoryIdIgnoreCase(String activeDirectoryId);

    List<User> getUsersByArchivedTrue();

    List<User> getUsersByEnabledFalse();

    List<User> getUsersByTeacherIsTrue();

    List<User> getUsersByGaGraderIsTrue();

    List<User> getUsersByReadyGraderIsTrue();

    List<User> getUsersByAccessId(int accessId);

    List<User> getUsersByTheologicalStudentIsTrue();

    @Query("select accessId from User u where u.id = :userId")
    int getAccessIdByUserId(@Param("userId") Integer userId);

    @Query("select roleId from User u where u.id = :userId")
    int getRoleIdByUserId(@Param("userId") Integer userId);

}
