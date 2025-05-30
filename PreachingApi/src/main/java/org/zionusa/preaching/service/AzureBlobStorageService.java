package org.zionusa.preaching.service;

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
import org.zionusa.preaching.domain.PreachingMaterial;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AzureBlobStorageService {

    private static final Logger log = LoggerFactory.getLogger(AzureBlobStorageService.class);

    private CloudBlobContainer preachingMaterialsContainer;

    @Value("${azure.storage.connection-string}")
    private String azureConnectionString;

    @Value("${blob.preaching-materials}")
    private String eventsPreachingMaterialsContainer;

    @Value("${azure.storage.url}")
    private String azureBlobUrl;

    @PostConstruct
    public void init() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(azureConnectionString);
        CloudBlobClient client = cloudStorageAccount.createCloudBlobClient();
        preachingMaterialsContainer = client.getContainerReference(eventsPreachingMaterialsContainer);
    }


    public String uploadPreachingMaterials(PreachingMaterial preachingMaterial, MultipartFile file) {
        log.info("started uploading preaching materials for {} (fileSize: {})", preachingMaterial.getId(), file.getSize());

        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String timestamp = new SimpleDateFormat("YYYY-MMM-dd-HH-mm-ss").format(new Date());
            String filename = preachingMaterial.getId() + "-" + timestamp + "."+ extension ;
            CloudBlockBlob blob = preachingMaterialsContainer.getBlockBlobReference(filename);
            blob.uploadFromByteArray(file.getBytes(), 0, file.getBytes().length);
            log.info("upload of preaching material {} successfully", preachingMaterial.getId());

            // set the url and return
            return azureBlobUrl + eventsPreachingMaterialsContainer + '/' + filename;

        } catch (IOException e) {
            System.out.println(e);
        } catch (StorageException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

}
