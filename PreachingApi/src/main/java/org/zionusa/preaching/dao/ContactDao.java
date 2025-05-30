package org.zionusa.preaching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zionusa.preaching.domain.Contact;

import java.util.Date;
import java.util.List;

public interface ContactDao extends JpaRepository<Contact, Integer> {

    List<Contact> getByUserId1OrUserId2OrUserId3(Integer userId1, Integer userId2, Integer userId3);

    @Query("select c from Contact c where (userId1 = :userId or userId2 = :userId or userId3 = :userId) and date between :startDate and :endDate")
    List<Contact> getByUserIdAndDate(@Param("userId") Integer userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
