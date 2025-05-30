package org.zionusa.event.domain.contact;

import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.base.util.exceptions.NotFoundException;
import org.zionusa.event.domain.contact.Contact;
import org.zionusa.event.domain.contact.ContactsService;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactsController extends BaseController<Contact, Integer> {

    private final ContactsService contactsService;

    public ContactsController(ContactsService contactsService) {
        super(contactsService);
        this.contactsService = contactsService;
    }

    @GetMapping(value = "/active")
    public List<Contact> active() {
        return contactsService.active();
    }

    @PutMapping(value = "/{id}/archive")
    public void archive(@PathVariable Integer id) throws NotFoundException {
        contactsService.archive(id);
    }
}
