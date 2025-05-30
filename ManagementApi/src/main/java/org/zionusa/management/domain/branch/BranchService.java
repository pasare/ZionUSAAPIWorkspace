package org.zionusa.management.domain.branch;

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
public class BranchService extends BaseService<Branch, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(BranchService.class);

    private final BranchViewService branchViewService;
    private final BranchDao dao;
    private final FileService fileService;

    @Autowired
    BranchService(BranchViewService branchViewService, BranchDao dao, FileService fileService) {
        super(dao, logger, Branch.class);
        this.branchViewService = branchViewService;
        this.dao = dao;
        this.fileService = fileService;
    }

    @Override
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        return branchViewService.getAllDisplay(columns);
    }

    public List<Branch> getAllByParentBranchId(Integer parentBranchId) {
        return dao.getAllByParentBranchId(parentBranchId);
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
        Optional<Branch> branchOptional = dao.findById(id);

        if (branchOptional.isPresent()) {
            FileResponse fileResponse = fileService.upload(displayName, multipartFile);

            Branch branch = branchOptional.get();
            branch.setPictureFileId(fileResponse.getPicture().getId());
            branch.setThumbnailFileId(fileResponse.getThumbnail().getId());

            dao.save(branch);
        }
    }
}
