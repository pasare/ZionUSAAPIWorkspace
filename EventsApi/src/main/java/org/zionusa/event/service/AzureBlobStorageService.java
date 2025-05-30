package org.zionusa.event.service;

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

    private CloudBlobContainer additionalImagesContainer;

    private CloudBlobContainer qrContainer;

    @Value("${azure.storage.connection-string}")
    private String azureConnectionString;

    @Value("${blob.image.container}")
    private String eventsImageContainer;

    @Value("${blob.image.additional.container}")
    private String eventsAdditionalImagesContainer;

    @Value("${blob.image.qr}")
    private String eventsQrCodeContainer;

    @Value("${azure.storage.url}")
    private String azureBlobUrl;

    @PostConstruct
    public void init() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount cloudStorageAccount = CloudStorageAccount.parse(azureConnectionString);
        CloudBlobClient client = cloudStorageAccount.createCloudBlobClient();
        imageContainer = client.getContainerReference(eventsImageContainer);
        additionalImagesContainer = client.getContainerReference(eventsAdditionalImagesContainer);
        qrContainer = client.getContainerReference(eventsQrCodeContainer);
    }


    public String uploadEventFile(Integer userId, Integer eventProposalId, MultipartFile file, String name, String type) {
        log.info("User {} started uploading event picture for {} (fileSize: {}) ", userId, eventProposalId, file.getSize());

        CloudBlobContainer container;
        String containerPath;

        if (type.toLowerCase().replace("\\s", "-").equals("event-picture")) {
            container = imageContainer;
            containerPath = eventsImageContainer;
        } else {
            container = additionalImagesContainer;
            containerPath = eventsAdditionalImagesContainer;
        }

        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            String filename = eventProposalId + "-" + Long.toString(timestamp) + "."+ extension ;
            CloudBlockBlob blob = container.getBlockBlobReference(filename);
            blob.uploadFromByteArray(file.getBytes(), 0, file.getBytes().length);
            log.info("User {} uploaded to event proposal {} successfully", userId, eventProposalId);

            // set the url and return
            return azureBlobUrl + containerPath + '/' + filename;

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (StorageException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String uploadQrCodeImage(Integer eventProposalId, MultipartFile file, String name) {
        log.info("started uploading QR Code for {} (fileSize: {})", eventProposalId, file.getSize());

        try {
            String filename = "event-proposal-" + eventProposalId + "/" + name.replace("\\s", "-").toLowerCase();
            CloudBlockBlob blob = qrContainer.getBlockBlobReference(filename);
            blob.uploadFromByteArray(file.getBytes(), 0, file.getBytes().length);
            log.info("uploaded to event proposal {} successfully", eventProposalId);

            // set the url and return
            return azureBlobUrl + eventsQrCodeContainer + '/' + filename;

        } catch (IOException e) {
            System.out.println(e);
        } catch (StorageException | URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

}
