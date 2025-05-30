package org.zionusa.preaching.service;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.base.service.BaseService;
import org.zionusa.preaching.dao.PreachingMaterialDao;
import org.zionusa.preaching.domain.PreachingMaterial;

import java.util.List;
import java.util.Optional;

@Service
public class PreachingMaterialService extends BaseService<PreachingMaterial> {
    private static final Logger logger = LoggerFactory.getLogger(PreachingMaterialService.class);

    private final PreachingMaterialDao preachingMaterialDao;
    private final AzureBlobStorageService azureBlobStorageService;

    @Autowired
    public PreachingMaterialService(PreachingMaterialDao preachingMaterialDao, PreachingMaterialDao preachingMaterialDao1, AzureBlobStorageService azureBlobStorageService) {
        super(preachingMaterialDao, logger, PreachingMaterial.class);
        this.preachingMaterialDao = preachingMaterialDao1;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    public PreachingMaterial upload(Integer id, MultipartFile file) throws NotFoundException {
        Optional<PreachingMaterial> preachingMaterialOptional = preachingMaterialDao.findById(id);

        if (!preachingMaterialOptional.isPresent()) {
            throw new NotFoundException("Cannot find the preaching material within our system to connect to the document");
        }

        PreachingMaterial preachingMaterial = preachingMaterialOptional.get();
        String materialUrl = azureBlobStorageService.uploadPreachingMaterials(preachingMaterial, file);
        preachingMaterial.setMaterialUrl(materialUrl);

        return preachingMaterialDao.save(preachingMaterial);
    }

    public List<PreachingMaterial> getByLanguageCode (String languageCode) {
        return this.preachingMaterialDao.getPreachingMaterialByLanguageCode(languageCode);
    }

    public List<PreachingMaterial> getByLanguageCodeAndId (String languageCode, Integer id) {
        return this.preachingMaterialDao.getPreachingMaterialByLanguageCodeAndId(languageCode, id);
    }
}
