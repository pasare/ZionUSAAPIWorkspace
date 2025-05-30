package org.zionusa.preaching.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.preaching.domain.SermonPreachingVerse;

public interface SermonPreachingVerseDao extends JpaRepository<SermonPreachingVerse, Integer> {
}
