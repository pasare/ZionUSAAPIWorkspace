package org.zionusa.preaching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.preaching.domain.SermonPreaching;

import java.util.List;

public interface SermonPreachingDao extends JpaRepository<SermonPreaching, Integer> {

    List<SermonPreaching> getSermonPreachingByBookNumber(Integer bookNumber);

    List<SermonPreaching> getSermonPreachingByBookNumberAndChapterNumber(Integer bookNumber, Integer chapterNumber);

    List<SermonPreaching> getSermonPreachingByTitle(String title);

    List<SermonPreaching> getSermonPreachingByLanguageCode(String languageCode);

    List<SermonPreaching> getSermonPreachingByLanguageCodeAndId(String languageCode, Integer id);


}
