package org.zionusa.management.domain.branch;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.base.controller.BaseController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/branches")
@Api(value = "Branches")
public class BranchController extends BaseController<Branch, Integer> {

    private final BranchService service;

    @Autowired
    public BranchController(BranchService service) {
        super(service);
        this.service = service;
    }

    @GetMapping(value = "/{id}/branches")
    @ApiOperation(value = "View a list of branches which are a branch of the provided id", response = List.class)
    public List<Branch> getAllByParentBranchId(@PathVariable Integer id) {
        return service.getAllByParentBranchId(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/picture/upload")
    public void pictureUpload(@PathVariable Integer id,
                              @RequestParam(value = "displayName", required = false) String displayName,
                              @RequestParam("file") MultipartFile file) throws IOException, ImageProcessingException, MetadataException {
        service.pictureUpload(id, displayName, file);
    }

}
