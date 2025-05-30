package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);


    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    public EmailService(MessageSource messageSource, JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //  helper function
    public void sendHtmlEmail(String to, String bcc, String subject, String text) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(text, true);

        if (to.contains(",")) {
            helper.setTo(to.split(","));

            logger.warn("Recipient  emails 1: {}",  to);

        } else {
            helper.setTo(to);
        }

        if (bcc != null)
            if (bcc.contains(",")) {
                logger.warn("Bcc emails logging: {}", bcc);
                helper.setBcc(bcc.split(","));
            } else {
                helper.setBcc(bcc);
                logger.error("Recipient  emails 2: {}",  to);

            }

        javaMailSender.send(msg);
    }

    public void sendHtmlEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(text, true);

        if (to.contains(",")) {
            helper.setTo(to.split(","));
        } else {
            helper.setTo(to);
        }
        javaMailSender.send(msg);
    }
}
