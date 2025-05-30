package org.zionusa.preaching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.preaching.dao.SermonPreachingVerseDao;
import org.zionusa.preaching.domain.SermonPreachingVerse;

@Service
public class SermonPreachingVerseService extends BaseService<SermonPreachingVerse> {

    private static final Logger logger = LoggerFactory.getLogger(SermonPreachingService.class);

    private final SermonPreachingVerseDao sermonPreachingVerseDao;

    @Autowired
    public SermonPreachingVerseService(SermonPreachingVerseDao sermonPreachingVerseDao) {
        super(sermonPreachingVerseDao, logger, SermonPreachingVerse.class);
        this.sermonPreachingVerseDao = sermonPreachingVerseDao;
    }

}
