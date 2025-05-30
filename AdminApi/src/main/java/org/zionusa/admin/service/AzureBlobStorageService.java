package org.zionusa.admin.service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AzureBlobStorageService {

    private static final Logger log = LoggerFactory.getLogger(AzureBlobStorageService.class); 

    private CloudBlobContainer imageContainer;

    @Value("${azure.storage.connection-string}")
    private String azureConnectionString;

    @Value("${blob.image.container}")
    private String imageContainerUrl;

    @Value("${azure.storage.url}")
    private String azureBlobUrl;

    @PostConstruct
    public void init() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(azureConnectionString);
        CloudBlobClient client = cloudStorageAccount.createCloudBlobClient();
        imageContainer = client.getContainerReference(imageContainerUrl);
    }


    public String uploadImage(Integer userId, MultipartFile file, String name, String section) {
        log.info("User {} started uploading image (fileSize: {})", userId, file.getSize());

        CloudBlobContainer container;
        container = imageContainer;

        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            String filename = section +"/"+ name.replaceAll("\\s", "-") + "-" + Long.toString(timestamp) + "."+ extension ;
            CloudBlockBlob blob = container.getBlockBlobReference(filename);
            blob.uploadFromByteArray(file.getBytes(), 0, file.getBytes().length);
            log.info("User {} uploaded to {} successfully", userId, section);

            // set the url and return
            return azureBlobUrl + imageContainerUrl + '/' + filename;

        } catch (IOException e) {
            System.out.println(e);
        } catch (StorageException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

}
