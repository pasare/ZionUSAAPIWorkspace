package org.zionusa.preaching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.preaching.domain.PreachingMaterial;

import java.util.List;

public interface PreachingMaterialDao extends JpaRepository<PreachingMaterial, Integer> {

    List<PreachingMaterial> getPreachingMaterialByLanguageCode(String languageCode);

    List<PreachingMaterial> getPreachingMaterialByLanguageCodeAndId(String languageCode, Integer id);
}
