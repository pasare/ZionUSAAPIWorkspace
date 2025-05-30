package org.zionusa.management.domain.translation;

import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface TranslationDao extends BaseDao<Translation, Integer> {

    List<Translation> getTranslationByLanguage(String language);

}
