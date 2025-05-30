package org.zionusa.management.domain.association;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.management.domain.file.FileResponse;
import org.zionusa.management.domain.file.FileService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AssociationService extends BaseService<Association, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AssociationService.class);

    private final AssociationViewService associationViewService;
    private final AssociationDao dao;
    private final FileService fileService;

    @Autowired
    AssociationService(AssociationDao dao, AssociationViewService associationViewService, FileService fileService) {
        super(dao, logger, Association.class);
        this.associationViewService = associationViewService;
        this.dao = dao;
        this.fileService = fileService;
    }

    @Override
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        return associationViewService.getAllDisplay(columns);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Association patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Association save(Association t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<Association> saveMultiple(List<Association> tList) {
        return super.saveMultiple(tList);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void delete(Integer id) throws NotFoundException {
        super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteMultiple(String ids) throws NotFoundException {
        super.deleteMultiple(ids);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @Transactional
    public void pictureUpload(Integer id, String displayName, MultipartFile multipartFile) throws IOException, ImageProcessingException, MetadataException {
        Optional<Association> associationOptional = dao.findById(id);

        if (associationOptional.isPresent()) {
            FileResponse fileResponse = fileService.upload(displayName, multipartFile);

            Association association = associationOptional.get();
            association.setPictureFileId(fileResponse.getPicture().getId());
            association.setThumbnailFileId(fileResponse.getThumbnail().getId());

            dao.save(association);
        }
    }
}
