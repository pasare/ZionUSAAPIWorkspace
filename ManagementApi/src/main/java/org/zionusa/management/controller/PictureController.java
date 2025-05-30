package org.zionusa.management.controller;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.management.domain.Church;
import org.zionusa.management.domain.User;
import org.zionusa.management.service.AzureBlobStorageService;
import org.zionusa.management.service.ChurchService;
import org.zionusa.management.service.PictureService;
import org.zionusa.management.service.UserService;
import org.zionusa.management.util.PictureUtil;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pictures")
public class PictureController {

    private static final int FULL_IMAGE_WIDTH = 640;
    private static final int FULL_IMAGE_HEIGHT = 640;
    private static final int MEDIUM_IMAGE_WIDTH = 240;
    private static final int MEDIUM_IMAGE_HEIGHT = 240;
    private static final int THUMB_IMAGE_WIDTH = 80;
    private static final int THUMB_IMAGE_HEIGHT = 80;

    private final PictureService pictureService;
    private final UserService userService;
    private final ChurchService churchService;
    private final AzureBlobStorageService azureBlobStorageService;

    @Autowired
    public PictureController(PictureService pictureService, UserService userService, ChurchService churchService, AzureBlobStorageService azureBlobStorageService) {
        this.pictureService = pictureService;
        this.userService = userService;
        this.churchService = churchService;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    @DeleteMapping(value = "/branch/{id}")
    public void deleteBranchPicture(@PathVariable Integer id) {
        pictureService.deleteChurchPicture(id);
    }

    @Deprecated
    @DeleteMapping(value = "/church/{id}")
    public void deleteChurchPicture(@PathVariable Integer id) {
        pictureService.deleteChurchPicture(id);
    }

    @DeleteMapping(value = "/user/{id}")
    public void deleteUserPicture(@PathVariable Integer id) {
        pictureService.deleteUserPicture(id);
    }

    @GetMapping(value = "/church/{churchId}")
    public List<Church.ChurchPicture> getChurchPictures(@PathVariable("churchId") Integer churchId) {
        return pictureService.getChurchPicturesByChurchId(churchId);
    }

    @GetMapping(value = "/church/{churchId}/profile")
    public Church.ChurchPicture getChurchProfilePicture(@PathVariable("churchId") Integer churchId) {
        return pictureService.getChurchProfilePicture(churchId);
    }

    @GetMapping(value = "/user/{userId}/profile")
    public User.UserPicture getUserProfilePicture(@PathVariable("userId") Integer userId) {
        return pictureService.getUserProfilePicture(userId);
    }

    @PostMapping(value = "/church/{churchId}")
    public Church.ChurchPicture saveChurchPicture(@PathVariable("churchId") Integer churchId,
                                                  @RequestParam("name") String name,
                                                  @RequestParam(value = "desc", required = false) String desc,
                                                  @RequestParam("picture") MultipartFile picture) throws IOException, ImageProcessingException, MetadataException {
        Church.ChurchPicture churchPicture = new Church.ChurchPicture();
        churchPicture.setChurchId(churchId);
        churchPicture.setTypeId(1);
        churchPicture.setName(name);
        churchPicture.setDescription(desc);
        churchPicture.setPictureFull(PictureUtil.cropImageSquare(picture.getBytes()));
        churchPicture.setPictureMedium(PictureUtil.resizeImage(picture.getBytes(), MEDIUM_IMAGE_WIDTH, MEDIUM_IMAGE_HEIGHT));
        churchPicture.setThumbnail(PictureUtil.resizeImage(picture.getBytes(), THUMB_IMAGE_WIDTH, THUMB_IMAGE_HEIGHT));

        //upload the user picture to blob storage
        try {
            azureBlobStorageService.uploadChurchPicture(churchPicture);
        } catch (Exception e) {
            throw new IOException("There as a problem uploading the image");
        }

        return pictureService.saveChurchPicture(churchPicture);
    }

    @PostMapping(value = "/church/{churchId}/profile")
    public Church.ChurchPicture saveChurchProfilePicture(@PathVariable("churchId") Integer churchId,
                                                         @RequestParam("name") String name,
                                                         @RequestParam(value = "desc", required = false) String desc,
                                                         @RequestParam("picture") MultipartFile picture) throws IOException, ImageProcessingException, MetadataException {

        Church.ChurchPicture churchPicture = pictureService.getChurchProfilePicture(churchId);
        if (churchPicture == null)
            churchPicture = new Church.ChurchPicture();

        Church church = churchService.getById(churchId);

        // check if picture already exists
        if (church != null) {
            churchPicture.setChurchId(churchId);
            churchPicture.setTypeId(2);
            churchPicture.setName(church.getName().replaceAll("\\s", "") + "-Profile-Picture_" + System.currentTimeMillis());
            churchPicture.setDescription(desc);
            churchPicture.setPictureFull(PictureUtil.cropImageSquare(picture.getBytes()));
            churchPicture.setPictureMedium(PictureUtil.resizeImage(picture.getBytes(), MEDIUM_IMAGE_WIDTH, MEDIUM_IMAGE_HEIGHT));
            churchPicture.setThumbnail(PictureUtil.resizeImage(picture.getBytes(), THUMB_IMAGE_WIDTH, THUMB_IMAGE_HEIGHT));

            //upload the user picture to blob storage
            try {
                azureBlobStorageService.uploadChurchPicture(churchPicture);
            } catch (Exception e) {
                throw new IOException("There was a problem uploading the image");
            }

            return pictureService.saveChurchPicture(churchPicture);
        }

        return null;
    }

    @PostMapping(value = "/user/{userId}")
    public User.UserPicture saveUserPicture(@PathVariable("userId") Integer userId,
                                            @RequestParam("name") String name,
                                            @RequestParam(value = "desc", required = false) String desc,
                                            @RequestParam("picture") MultipartFile picture) throws IOException, ImageProcessingException, MetadataException {
        User.UserPicture userPicture = new User.UserPicture();
        userPicture.setUserId(userId);
        userPicture.setName(name);
        userPicture.setDescription(desc);
        userPicture.setPictureFull(PictureUtil.cropImageSquare(picture.getBytes()));
        userPicture.setPictureMedium(PictureUtil.resizeImage(picture.getBytes(), MEDIUM_IMAGE_WIDTH, MEDIUM_IMAGE_HEIGHT));
        userPicture.setThumbnail(PictureUtil.resizeImage(picture.getBytes(), THUMB_IMAGE_WIDTH, THUMB_IMAGE_HEIGHT));
        return pictureService.saveUserPicture(userPicture);
    }

    @PostMapping(value = "/user/{userId}/profile")
    public User.UserPicture saveUserProfilePicture(@PathVariable("userId") Integer userId,
                                                   @RequestParam(value = "desc", required = false) String desc,
                                                   @RequestParam("picture") MultipartFile picture) throws IOException, ImageProcessingException, MetadataException, NotFoundException {

        User.UserPicture userPicture = pictureService.getUserProfilePicture(userId);
        if (userPicture == null)
            userPicture = new User.UserPicture();

        User user = userService.getById(userId);

        if (user != null) {
            User.UserPicture savedUserPicture;
            userPicture.setUserId(userId);
            userPicture.setName(user.getDisplayName().replaceAll("\\s", "") + "-Profile-Picture_" + System.currentTimeMillis());
            userPicture.setDescription(desc);
            userPicture.setPictureFull(PictureUtil.cropImageSquare(picture.getBytes()));
            userPicture.setPictureMedium(PictureUtil.resizeImage(picture.getBytes(), MEDIUM_IMAGE_WIDTH, MEDIUM_IMAGE_HEIGHT));
            userPicture.setThumbnail(PictureUtil.resizeImage(picture.getBytes(), THUMB_IMAGE_WIDTH, THUMB_IMAGE_HEIGHT));
            //upload the user picture to blob storage
            try {
                azureBlobStorageService.uploadUserPicture(userPicture);
                savedUserPicture = pictureService.saveUserPicture(userPicture);
                user.setPictureId(savedUserPicture.getId());
                //userService.save(user);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("There was a problem uploading the image", e);
            }

            return savedUserPicture;
        }

        return null;
    }
}
