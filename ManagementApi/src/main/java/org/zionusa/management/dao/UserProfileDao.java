package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.userprofile.UserProfile;

import java.util.List;

public interface UserProfileDao extends JpaRepository<UserProfile, Integer> {

    UserProfile findUserProfileByUserId(Integer userId);

    List<UserProfile> findUserProfilesByChurchId(Integer churchId);
}
