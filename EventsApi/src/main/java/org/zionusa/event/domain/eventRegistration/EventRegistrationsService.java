package org.zionusa.event.domain.eventRegistration;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.base.service.BaseService;
import org.zionusa.event.domain.eventProposal.EventProposal;
import org.zionusa.event.domain.eventProposal.EventProposalsDao;
import org.zionusa.event.service.AzureBlobStorageService;
import org.zionusa.event.service.EmailService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.io.IOUtils.toByteArray;

@Service
public class EventRegistrationsService extends BaseService<EventRegistration, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventRegistrationsService.class);

    private final EventRegistrationsDao eventRegistrationsDao;
    private final EventProposalsDao eventProposalsDao;
    private final AzureBlobStorageService azureBlobStorageService;
    private final EmailService emailService;

    @Autowired
    public EventRegistrationsService(EventRegistrationsDao eventRegistrationsDao, EventProposalsDao eventProposalsDao, AzureBlobStorageService azureBlobStorageService, EmailService emailService) {
        super(eventRegistrationsDao, logger, EventRegistration.class);
        this.eventRegistrationsDao = eventRegistrationsDao;
        this.eventProposalsDao = eventProposalsDao;
        this.azureBlobStorageService = azureBlobStorageService;
        this.emailService = emailService;
    }

    public List<EventRegistration> getByEventId(Integer id) {
        logger.warn("Attempting to get EventRegistrations for EventProposal: {} ", id);
        return eventRegistrationsDao.getEventRegistrationsByEventProposalId(id);
    }

    public List<EventRegistration> getByUserId(Integer id) {
        logger.warn("Attempting to get EventRegistrations for user: {} ", id);
        return eventRegistrationsDao.getEventRegistrationsByParticipantId(id);
    }

    @Override
    public EventRegistration save(EventRegistration eventRegistration) {

        // If this is a new event registration we need to send an email with the qr code and event details
        if (eventRegistration.getId() == null && eventRegistration.getParticipantEmail() != null) {

            try {
                String qrCodePath = createQrCode(eventRegistration);
                if (eventRegistration.getSendConfirmationEmail() != null && eventRegistration.getSendConfirmationEmail()) {
                    sendRegistrationEmail(eventRegistration);
                }
            } catch (Exception e) {
                logger.error("The qr code could not be generated", e);
            }
        }
        return eventRegistrationsDao.save(eventRegistration);
    }

    public void sendRegistrationEmail(EventRegistration eventRegistration) {

        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(eventRegistration.getEventProposalId());

        if (eventProposalOptional.isPresent()) {

            EventProposal eventProposal = eventProposalOptional.get();
            String to = eventRegistration.getParticipantEmail();
            StringBuilder message = new StringBuilder();
            LocalDate eventDate = LocalDate.parse(eventProposal.getProposedDate());
            if (eventProposal.getTitle().contains("Food Festival")) {
                message.append("Thank you for registering for ").append(eventProposal.getTitle()).append("!").append("</br>");
                message.append("The event will be on ").append(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate))
                    .append(" from ").append(eventProposal.getProposedTime())
                    .append(" to ").append(eventProposal.getProposedEndTime())
                    .append("</br></br>");
                message.append("<b>Event Program</b>").append("<br/>");
                message.append("<ul>")
                    .append("<li>").append("12 p.m | Doors Open").append("</li>")
                    .append("<li>").append("12-3 p.m | Food and games").append("</li>")
                    .append("<li>").append("3 p.m | Concert & raffle").append("</li>")
                    .append("</ul>").append("<br/>");
                message.append("<b>Things You Should Know</b>").append("<br/>");
                message.append("<ul>")
                    .append("<li>").append("Entry is $25 for adults, and children below 12 enter free of charge. The fee is for food tickets and a gift upon entry. Only cash will be accepted.").append("</li>")
                    .append("<li>").append("Each participant will receive 5 tickets at the door to use at the food stations.").append("</li>")
                    .append("<li>").append("Additional food tickets will be available for purchase on-site for $2 per ticket").append("</li>")
                    .append("<li>").append("There will be a raffle, and the winner will be announced after the 3 p.m. concert").append("</li>").append("</ul>");
                message.append("We look forward to seeing you there!");
            } else {
                message.append("Thank you for registering for the upcoming event ( ").append(eventProposal.getTitle()).append(").")
                    .append("</br></br>");
                message.append("The event is scheduled to take place on: ").append(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(eventDate))
                    .append(" @ ").append(eventProposal.getProposedTime()).append("</br>");
                message.append("The below code can be used to confirm your reservation at the door.").append("<br/><br/><br/>");
                message.append("<img src='").append(eventRegistration.getQrImageUrl()).append("' />");
            }
            try {
                this.emailService.sendHtmlEmail(to, eventProposal.getTitle() + " Registration Confirmation", message.toString());
            } catch (Exception e) {
                logger.warn("Registration confirmation failed to send");
            }
        } else {
            logger.error("The event does not exist, unable to send confirmation email");
        }
    }

    public String createQrCode(EventRegistration eventRegistration) throws IOException {
        Optional<EventProposal> eventProposalOptional = eventProposalsDao.findById(eventRegistration.getEventProposalId());

        if (eventProposalOptional.isPresent()) {
            EventProposal eventProposal = eventProposalOptional.get();

            String qrText = eventProposal.getId().toString() + "-" + eventProposal.getTitle() + "-" + eventRegistration.getParticipantEmail();
            File file = QRCode.from(qrText).to(ImageType.PNG)
                .withSize(200, 200)
                .file();

            String fileName = eventProposal.getTitle() + "-" + eventRegistration.getParticipantEmail() + ".png";

            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/png", toByteArray(fileInputStream));

            // upload the file to azure
            String imageUrl = this.azureBlobStorageService.uploadQrCodeImage(eventProposal.getId(), multipartFile, fileName);

            eventRegistration.setQrCode(qrText);
            eventRegistration.setQrImageUrl(imageUrl);

            System.out.println("Event Regis in QrCode: " + eventRegistration);
            eventRegistrationsDao.save(eventRegistration);

            return qrText;
        } else {
            logger.error("The event proposal could not be found in the system");
        }

        return null;
    }
}
