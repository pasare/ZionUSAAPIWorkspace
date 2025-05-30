package org.zionusa.management.domain.translation;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/translations")
@Api(value = "Translations")

public class TranslationController extends BaseController<Translation, Integer> {

    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        super(translationService);
        this.translationService = translationService;
    }

    @GetMapping(value = "/language/{language}")
    public List<Translation> getTranslationByLanguage(@PathVariable String language) {
        return translationService.getTranslationByLanguage(language);
    }

    @GetMapping(value = "/local")
    public List<Map<String, String>> getLocalTranslations() {
        return translationService.getLocalTranslations();
    }

}
