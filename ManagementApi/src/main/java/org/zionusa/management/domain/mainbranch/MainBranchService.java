package org.zionusa.management.domain.mainbranch;

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
public class MainBranchService extends BaseService<MainBranch, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(MainBranchService.class);

    private final MainBranchDao dao;
    private final FileService fileService;
    private final MainBranchViewService mainBranchViewService;

    @Autowired
    MainBranchService(MainBranchDao dao, FileService fileService, MainBranchViewService mainBranchViewService) {
        super(dao, logger, MainBranch.class);
        this.dao = dao;
        this.fileService = fileService;
        this.mainBranchViewService = mainBranchViewService;
    }

    @Override
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        return mainBranchViewService.getAllDisplay(columns);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public MainBranch patchById(Integer id, Map<String, Object> fields) throws NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public MainBranch save(MainBranch t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<MainBranch> saveMultiple(List<MainBranch> tList) {
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
        Optional<MainBranch> mainBranchOptional = dao.findById(id);

        if (mainBranchOptional.isPresent()) {
            FileResponse fileResponse = fileService.upload(displayName, multipartFile);

            MainBranch mainBranch = mainBranchOptional.get();
            mainBranch.setPictureFileId(fileResponse.getPicture().getId());
            mainBranch.setThumbnailFileId(fileResponse.getThumbnail().getId());

            dao.save(mainBranch);
        }
    }
}
