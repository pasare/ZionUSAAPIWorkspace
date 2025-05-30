package org.zionusa.event.domain.contact;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.event.domain.contact.Contact;

import java.util.List;

public interface ContactsDao extends BaseDao<Contact, Integer> {
    List<Contact> findAllByArchivedIsNot(Boolean archived);
}
