package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.UserRegistration;

import java.util.List;
import java.util.Optional;

public interface UserRegistrationDao extends JpaRepository<UserRegistration, Integer> {
    List<UserRegistration> findAllByChurchId(Integer churchId);

    List<UserRegistration> findAllByChurchIdAndApprovedIsNull(Integer churchId);

    List<UserRegistration> findAllByChurchIdAndApprovedIsNullAndProcessedNot(Integer churchId, Boolean processed);

    Optional<UserRegistration> findByEmail(String email);

    Integer countAllByChurchIdAndApprovedNullAndProcessedIsNull(Integer churchId);
}
