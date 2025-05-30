package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.management.domain.Church;

import java.util.List;

public interface ChurchPictureDao extends JpaRepository<Church.ChurchPicture, Integer> {

    List<Church.ChurchPicture> findAllByChurchIdAndTypeId(Integer churchId, Integer typeId);

    Church.ChurchPicture findByChurchIdAndTypeId(Integer churchId, Integer typeId);
}
