package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.UserProfileInformation;

import java.util.List;

public interface UserProfileInformationDao extends JpaRepository<UserProfileInformation, Integer> {

    List<UserProfileInformation> getUserProfileInformationByProfileId(Integer profileId);

    List<UserProfileInformation> getUserProfileInformationByCategoryIdAndProfileId(Integer categoryId, Integer profileId);

    void deleteUserProfileInformationByProfileIdAndCategoryIdNotIn(Integer profileId, List<Integer> categoryIds);
}
