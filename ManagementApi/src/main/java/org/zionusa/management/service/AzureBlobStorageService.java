package org.zionusa.management.service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zionusa.management.domain.Church;
import org.zionusa.management.domain.User;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Service
public class AzureBlobStorageService {

    private CloudBlobContainer userContainer;

    private CloudBlobContainer churchContainer;

    @Value("${azure.storage.connection-string}")
    private String azureConnectionString;

    @Value("${blob.user.picture.container}")
    private String userPicturesContainer;

    @Value("${blob.branch.picture.container}")
    private String churchPictureContainer;

    @Value("${azure.storage.url}")
    private String azureBlobUrl;

    @PostConstruct
    public void init() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(azureConnectionString);
        CloudBlobClient client = cloudStorageAccount.createCloudBlobClient();
        userContainer = client.getContainerReference(userPicturesContainer);
        churchContainer = client.getContainerReference(churchPictureContainer);
    }

    public void uploadUserPicture(User.UserPicture picture) {

        //   upload the file to blob storage
        try {
            String pictureFullName = (picture.getUserId() + "/" + picture.getName().replaceAll("\\s", "") + "-full.jpg").toLowerCase();
            String pictureMediumName = (picture.getUserId() + "/" + picture.getName().replaceAll("\\s", "") + "-medium.jpg").toLowerCase();
            String pictureThumbName = (picture.getUserId() + "/" + picture.getName().replaceAll("\\s", "") + "-thumb.jpg").toLowerCase();

            CloudBlockBlob blobFull = userContainer.getBlockBlobReference(pictureFullName);
            CloudBlockBlob blobMedium = userContainer.getBlockBlobReference(pictureMediumName);
            CloudBlockBlob blobThumb = userContainer.getBlockBlobReference(pictureThumbName);

            blobFull.uploadFromByteArray(picture.getPictureFull(), 0, picture.getPictureFull().length);
            blobMedium.uploadFromByteArray(picture.getPictureMedium(), 0, picture.getPictureMedium().length);
            blobThumb.uploadFromByteArray(picture.getThumbnail(), 0, picture.getThumbnail().length);

            // set the url and return
            picture.setPictureUrlFull(azureBlobUrl + userPicturesContainer + '/' + pictureFullName);
            picture.setPictureUrlMedium(azureBlobUrl + userPicturesContainer + '/' + pictureMediumName);
            picture.setThumbnailUrl(azureBlobUrl + userPicturesContainer + '/' + pictureThumbName);
        }
            catch (IOException e) {
                System.out.println(e);
            } catch (StorageException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void uploadChurchPicture(Church.ChurchPicture picture) {

        //upload the file to blob storage
        try {
            String pictureFullName = (picture.getChurchId() + "/" + picture.getName().replaceAll("\\s", "") + "-full.jpg").toLowerCase();
            String pictureMediumName = (picture.getChurchId() + "/" + picture.getName().replaceAll("\\s", "") + "-medium.jpg").toLowerCase();
            String pictureThumbName = (picture.getChurchId() + "/" + picture.getName().replaceAll("\\s", "") + "-thumb.jpg").toLowerCase();

            CloudBlockBlob blobFull = churchContainer.getBlockBlobReference(pictureFullName);
            CloudBlockBlob blobMedium = churchContainer.getBlockBlobReference(pictureMediumName);
            CloudBlockBlob blobThumb = churchContainer.getBlockBlobReference(pictureThumbName);

            blobFull.uploadFromByteArray(picture.getPictureFull(), 0, picture.getPictureFull().length);
            blobMedium.uploadFromByteArray(picture.getPictureMedium(), 0, picture.getPictureMedium().length);
            blobThumb.uploadFromByteArray(picture.getThumbnail(), 0, picture.getThumbnail().length);

            // set the url and return
            picture.setPictureUrlFull(azureBlobUrl + churchPictureContainer + '/' + pictureFullName);
            picture.setPictureUrlMedium(azureBlobUrl + churchPictureContainer + '/' + pictureMediumName);
            picture.setThumbnailUrl(azureBlobUrl + churchPictureContainer + '/' + pictureThumbName);
        }
        catch (IOException e) {
            System.out.println(e);
        } catch (StorageException | URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
