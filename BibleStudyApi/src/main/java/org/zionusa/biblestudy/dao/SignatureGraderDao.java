package org.zionusa.biblestudy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.Signature;

import java.util.List;

public interface SignatureGraderDao extends JpaRepository<Signature.SignatureGrader, String> {

    List<Signature.SignatureGrader> getAllByGraderId(Integer graderId);

    List<Signature.SignatureGrader> getAllByGraderIdAndGraderDateBetween(Integer graderId, String startDate, String endDate);

}
