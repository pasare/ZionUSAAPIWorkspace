package org.zionusa.management.domain.association;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.base.controller.BaseController;

import java.io.IOException;

@RestController
@RequestMapping("/associations")
public class AssociationController extends BaseController<Association, Integer> {

    private final AssociationService service;

    @Autowired
    public AssociationController(AssociationService service) {
        super(service);
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/picture/upload")
    public void pictureUpload(@PathVariable Integer id,
                              @RequestParam(value = "displayName", required = false) String displayName,
                              @RequestParam("file") MultipartFile file) throws IOException, ImageProcessingException, MetadataException {
        service.pictureUpload(id, displayName, file);
    }

}
