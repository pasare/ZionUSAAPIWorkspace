package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.UserPasswordReset;

import java.util.List;

public interface UserPasswordResetDao extends JpaRepository<UserPasswordReset, Integer> {
    UserPasswordReset getByUserName(String userName);

    List<UserPasswordReset> getAllByChurchIdAndApprovedNot(Integer churchId, boolean approved);

    List<UserPasswordReset> getAllByChurchIdAndApprovedIsNull(Integer churchId);

    Integer countAllByChurchIdAndApprovedNot(Integer churchId, boolean approved);

    Integer countAllByChurchIdAndApprovedIsNull(Integer churchId);
}
