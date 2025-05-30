package org.zionusa.management.domain.file;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.management.exception.NotFoundException;
import org.zionusa.management.util.PictureUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileService {

    private final FileDao dao;

    public FileService(FileDao dao) {
        this.dao = dao;
    }

    public File findById(String id) {
        Optional<File> fileOptional = dao.findById(id);

        if (!fileOptional.isPresent()) {
            throw new NotFoundException();
        }

        return fileOptional.get();
    }

    @Transactional
    public FileResponse upload(String displayName, MultipartFile multipartFile) throws IOException, ImageProcessingException, MetadataException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        byte[] squareFileBytes = PictureUtil.cropImageSquare(multipartFile.getBytes());
        // Resize image is able to convert PNG to JPEG
        byte[] pictureBytes = PictureUtil.resizeImage(squareFileBytes, 256, 256);
        byte[] thumbnailBytes = PictureUtil.resizeImage(squareFileBytes, 80, 80);

        File pictureFile = new File(
            displayName == null ? fileName : displayName,
            fileName,
            "image/jpeg",
            pictureBytes
        );

        File thumbnailFile = new File(
            displayName == null ? fileName : displayName,
            fileName,
            "image/jpeg",
            thumbnailBytes
        );

        FileResponse fileResponse = new FileResponse();

        File pictureFileResponse = dao.save(pictureFile);
        File thumbnailFileResponse = dao.save(thumbnailFile);

        fileResponse.setPicture(pictureFileResponse);
        fileResponse.setThumbnail(thumbnailFileResponse);

        return fileResponse;
    }
}
