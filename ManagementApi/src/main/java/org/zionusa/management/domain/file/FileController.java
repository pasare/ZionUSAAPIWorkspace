package org.zionusa.management.domain.file;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/files")
@Api(value = "Files")
public class FileController {
    private final FileService service;

    @Autowired
    public FileController(FileService service) {
        this.service = service;
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> findById(@PathVariable String id) {
        File file = service.findById(id);

        // Images can be cached for a year based on ID
        CacheControl cacheControl = CacheControl.maxAge(365, TimeUnit.DAYS)
            .noTransform()
            .mustRevalidate();

        return ResponseEntity.ok()
            .cacheControl(cacheControl)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
            .body(file.getData());
    }
}
