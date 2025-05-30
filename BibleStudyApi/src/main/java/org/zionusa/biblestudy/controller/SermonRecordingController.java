package org.zionusa.biblestudy.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.SermonRecording;
import org.zionusa.biblestudy.service.AzureBlobStorageService;
import org.zionusa.biblestudy.service.SermonRecordingService;

import java.util.List;

@RestController
@RequestMapping("/sermon-recordings")
public class SermonRecordingController extends BaseController<SermonRecording, Integer> {

    private final SermonRecordingService sermonRecordingService;
    private final AzureBlobStorageService azureBlobStorageService;

    @Autowired
    public SermonRecordingController(SermonRecordingService sermonRecordingService, AzureBlobStorageService azureBlobStorageService) {
        super(sermonRecordingService);
        this.sermonRecordingService = sermonRecordingService;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @GetMapping(value = "/parent-church/{churchId}")
    List<SermonRecording> getRecordingsByParentChurch(@PathVariable Integer churchId) {
        return sermonRecordingService.getByParentChurch(churchId);
    }

    @GetMapping(value = "/church/{churchId}")
    List<SermonRecording> getRecordingsByChurch(@PathVariable Integer churchId) {
        return sermonRecordingService.getByChurch(churchId);
    }

    @GetMapping(value = "/preacher/{preacherId}")
    List<SermonRecording> getRecordingsByPreacher(@PathVariable Integer preacherId) {
        return sermonRecordingService.getByPreacher(preacherId);
    }

    @PutMapping(value = "/{id}/grade")
    SermonRecording gradeRecording(@PathVariable Integer id, @RequestBody SermonRecording sermonRecording) throws NotFoundException {
        return sermonRecordingService.gradeSermon(sermonRecording);
    }

    @PostMapping(value = "/upload")
    String uploadAudioSermon(@RequestParam("preacherId") Integer preacherId,
                             @RequestParam("sermonTitle") String sermonTitle,
                             @RequestParam(value = "file") MultipartFile file) {
        return azureBlobStorageService.uploadSermonAudio(preacherId, sermonTitle, file);
    }
}
