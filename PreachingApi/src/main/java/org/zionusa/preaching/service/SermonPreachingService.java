package org.zionusa.preaching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.preaching.dao.SermonPreachingDao;
import org.zionusa.preaching.domain.SermonPreaching;

import java.util.List;

@Service
public class SermonPreachingService extends BaseService<SermonPreaching> {

    private static final Logger logger = LoggerFactory.getLogger(SermonPreachingService.class);

    private final SermonPreachingDao sermonPreachingDao;

    @Autowired
    public SermonPreachingService(SermonPreachingDao sermonPreachingDao) {
        super(sermonPreachingDao, logger, SermonPreaching.class);
        this.sermonPreachingDao = sermonPreachingDao;
    }

    /*@Override
    public List<SermonPreaching> getAll() {
        List<SermonPreaching> sermonPreachings = sermonPreachingDao.findAll();

        for (SermonPreaching sermonPreaching : sermonPreachings) {
            sermonPreaching.setVerses(new ArrayList<>());
        }
        return sermonPreachings;
    } */

    public List<SermonPreaching> getByBookNumber(Integer bookNumber) {
        return this.sermonPreachingDao.getSermonPreachingByBookNumber(bookNumber);
    }

    public List<SermonPreaching> getByBookAndChapterNumber(Integer bookNumber, Integer chapterNumber) {
        return this.sermonPreachingDao.getSermonPreachingByBookNumberAndChapterNumber(bookNumber, chapterNumber);
    }

    public List<SermonPreaching> getByTitle(String title) {
        return this.sermonPreachingDao.getSermonPreachingByTitle(title);
    }

    public List<SermonPreaching> getByLanguageCode(String languageCode) {

        return this.sermonPreachingDao.getSermonPreachingByLanguageCode(languageCode);
    }

    public List<SermonPreaching> getByIdAndLanguageCode( String languageCode, Integer id) {

        return this.sermonPreachingDao.getSermonPreachingByLanguageCodeAndId(languageCode, id);
    }

}
