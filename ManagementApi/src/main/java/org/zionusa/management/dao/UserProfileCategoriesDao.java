package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.UserProfileCategory;

public interface UserProfileCategoriesDao extends JpaRepository<UserProfileCategory, Integer> {
}
