package org.zionusa.management.domain.translation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.exception.NotFoundException;

import java.util.*;

import static org.zionusa.management.service.AuthenticatedUserService.log;

@Service
public class TranslationService extends BaseService<Translation, Integer> {

    private final TranslationDao translationDao;

    @Autowired
    public TranslationService(TranslationDao translationDao) {
        super(translationDao, log, Translation.class);
        this.translationDao = translationDao;
    }

    public List<Map<String, String>> getLocalTranslations() {
        List<Translation> translations = getAll(null);

        Map<String, Map<String, String>> translationsMap = new HashMap<>();
        translations.forEach(translation -> {
            if (translation.getName() != null && translation.getLanguage() != null) {
                translationsMap.putIfAbsent(translation.getName(), new HashMap<>());
                translationsMap.get(translation.getName()).putIfAbsent("key", translation.getName());
                translationsMap.get(translation.getName()).put(translation.getLanguage(), translation.getValue());
            }
        });

        return new ArrayList<>(translationsMap.values());
    }

    public List<Translation> getTranslationByLanguage(String language) {

        return translationDao.getTranslationByLanguage(language);
    }

    public Translation getTranslationById(Integer id) {
        Optional<Translation> translationOptional = translationDao.findById(id);

        if (!translationOptional.isPresent())
            throw new NotFoundException("The translation was not found");

        return translationOptional.get();
    }

    public void deleteTranslation(Integer id) {
        Translation translation = getById(id);

        if (translation == null)
            throw new NotFoundException("The translation is not the system.");

        translationDao.delete(translation);
    }

    public Translation saveTranslation(Translation translation) {

        return translationDao.save(translation);
    }


}
