package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.event.dao.EventPicturesDao;
import org.zionusa.event.domain.EventFile;

@Service
public class EventFilesService extends BaseService<EventFile, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(EventFilesService.class);
    private final EventPicturesDao dao;

    @Autowired
    public EventFilesService(EventPicturesDao dao) {
        super(dao, logger, EventFile.class);
        this.dao = dao;
    }

    public EventFile saveEventFileFromEventProposalUpload(Integer eventProposalId, String pictureBlobUrl, String title, String type) {
        EventFile eventFile = new EventFile();
        eventFile.setEventProposalId(eventProposalId);
        eventFile.setTitle(title);
        eventFile.setFileUrl(pictureBlobUrl);
        eventFile.setType(type);

        return dao.save(eventFile);
    }
}
