package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.User;

public interface UserPictureDao extends JpaRepository<User.UserPicture, Integer> {

    User.UserPicture findByUserId(Integer userId);
}
