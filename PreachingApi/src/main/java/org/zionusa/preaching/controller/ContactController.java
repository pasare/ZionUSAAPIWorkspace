package org.zionusa.preaching.controller;

import javassist.NotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.preaching.domain.Contact;
import org.zionusa.preaching.service.ContactService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController extends BaseController<Contact> {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        super(contactService);
        this.contactService = contactService;
    }

    @GetMapping(value = "/{userId}")
    List<Contact> getByUserId(@PathVariable Integer userId){
        return contactService.getByUserId(userId); //test
    }

    @GetMapping(value = "/{userId}/{startDate}/{endDate}")
    List<Contact> getByUserIdAndDate(@PathVariable Integer userId,
                                     @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                     @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){
        return contactService.getByUserIdAndDate(userId, startDate, endDate);
    }

    @PutMapping(value = "/baptize")
    Contact baptize(Contact contact) throws NotFoundException{
        return contactService.baptize(contact);
    }

    @PutMapping(value = "/un-baptize")
    Contact removeBaptism(Integer contactId){
        return contactService.removeBaptism(contactId);
    }

    @PutMapping(value = "/archive")
    void archive(Integer contactId){
        contactService.archive(contactId);
    }
}
