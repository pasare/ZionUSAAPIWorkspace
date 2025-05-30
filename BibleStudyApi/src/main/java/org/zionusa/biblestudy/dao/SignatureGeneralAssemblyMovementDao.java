package org.zionusa.biblestudy.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.biblestudy.domain.Signature;

import java.util.List;

public interface SignatureGeneralAssemblyMovementDao extends JpaRepository<Signature.SignatureGeneralAssemblyMovement, Integer> {

    Signature.SignatureGeneralAssemblyMovement getByTeacherId(Integer teacherId);

    List<Signature.SignatureGeneralAssemblyMovement> findAllBy(Pageable pageable);

}
