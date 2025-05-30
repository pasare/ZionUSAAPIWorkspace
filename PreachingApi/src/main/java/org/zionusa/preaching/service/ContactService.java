package org.zionusa.preaching.service;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.preaching.dao.ContactDao;
import org.zionusa.preaching.domain.Contact;
import org.zionusa.preaching.domain.PreachingLog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService extends BaseService<Contact> {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final ContactDao contactDao;
    private final PreachingLogService preachingLogService;

    @Autowired
    public ContactService(ContactDao contactDao, PreachingLogService preachingLogService) {
        super(contactDao, logger, Contact.class);
        this.contactDao = contactDao;
        this.preachingLogService = preachingLogService;
    }

    public List<Contact> getByUserId(Integer userId) {
        return contactDao.getByUserId1OrUserId2OrUserId3(userId, userId, userId);
    }

    public List<Contact> getByUserIdAndDate(Integer userId, Date startDate, Date endDate) {
        return contactDao.getByUserIdAndDate(userId, startDate, endDate);
    }

    @Override
    public Contact save(Contact contact) {
        logger.warn("Saving contact: {} on {}", contact.getFirstName() + ", " + contact.getLastName(), contact.getDate());
        //If the contact does not have a preaching log ID then it was made without a session so create one
        if (contact.getPreachingLogId() == null) {
            PreachingLog preachingLog = createContactRecord(contact);
            contact.setPreachingLogId(preachingLog.getId());
        }
        return contactDao.save(contact);
    }

    public Contact baptize(Contact contact) throws NotFoundException {
        Optional<Contact> contactOptional = contactDao.findById(contact.getId());

        if (!contactOptional.isPresent())
            throw new NotFoundException("The contact was not found in the system, cannot baptize");

        //create the preaching log to count this baptism
        PreachingLog preachingLog = createBaptismRecord(contact);
        Contact returnedContact = contactOptional.get();
        returnedContact.setBaptismDate(contact.getBaptismDate());
        returnedContact.setBaptismChurchId(contact.getBaptismChurchId());
        returnedContact.setBaptismLogId(preachingLog.getId());
        return contactDao.save(returnedContact);
    }

    public Contact removeBaptism(Integer contactId) {
        return null;
    }


    public void archive(Integer contactId) {

    }

    private PreachingLog createBaptismRecord(Contact contact) {
        logger.warn("Creating baptism record for: {} on {}", contact.getFirstName() + ", " + contact.getLastName(), contact.getBaptismDate());
        PreachingLog preachingLog = new PreachingLog();
        preachingLog.setUserId1(contact.getUserId1());
        preachingLog.setUserId2(contact.getUserId2());
        preachingLog.setUserId3(contact.getUserId3());
        preachingLog.setDate(contact.getBaptismDate());
        preachingLog.setBaptisms(1);
        preachingLog.setFruits(1);

        return preachingLogService.save(preachingLog);
    }

    private PreachingLog createContactRecord(Contact contact) {
        logger.warn("Creating out of season contact record for: {} on {}", contact.getFirstName() + ", " + contact.getLastName(), contact.getBaptismDate());
        PreachingLog preachingLog = new PreachingLog();
        preachingLog.setUserId1(contact.getUserId1());
        preachingLog.setUserId2(contact.getUserId2());
        preachingLog.setUserId3(contact.getUserId3());
        preachingLog.setDate(contact.getDate());
        preachingLog.setPreached(1);
        preachingLog.setContacts(1);

        return preachingLogService.save(preachingLog);
    }
}
