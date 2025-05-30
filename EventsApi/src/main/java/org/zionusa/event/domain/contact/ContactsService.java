package org.zionusa.event.domain.contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ContactsService extends BaseService<Contact, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ContactsService.class);
    private final ContactsDao contactsDao;

    @Autowired
    public ContactsService(ContactsDao contactsDao) {
        super(contactsDao, logger, Contact.class);
        this.contactsDao = contactsDao;
    }

    public List<Contact> active() {
        return contactsDao.findAllByArchivedIsNot(true);
    }

    public void archive(Integer id) {
        Optional<Contact> optionalContact = contactsDao.findById(id);

        if (!optionalContact.isPresent()) {
            throw new NotFoundException();
        }

        Contact contact = optionalContact.get();
        contact.setArchived(true);
        contactsDao.save(contact);
    }

}
