package org.zionusa.preaching.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.preaching.domain.SermonPreaching;
import org.zionusa.preaching.domain.SermonPreachingVerse;
import org.zionusa.preaching.service.SermonPreachingService;
import org.zionusa.preaching.service.SermonPreachingVerseService;

import java.util.List;

@RestController
@RequestMapping("/sermon-preaching")
public class SermonPreachingController extends BaseController<SermonPreaching> {

    private final SermonPreachingService sermonPreachingService;
    private final SermonPreachingVerseService sermonPreachingVerseService;

    @Autowired
    public SermonPreachingController(SermonPreachingService sermonPreachingService, SermonPreachingVerseService sermonPreachingVerseService) {
        super(sermonPreachingService);

        this.sermonPreachingService = sermonPreachingService;
        this.sermonPreachingVerseService = sermonPreachingVerseService;
    }

    @GetMapping(value = "/language")
    public  List<SermonPreaching> getAll(@RequestParam(required = false) String languageCode){
        if (languageCode == null){
            languageCode = "en";
        }
        return this.sermonPreachingService.getByLanguageCode(languageCode);
    }

    @GetMapping(value = "/language/{languageCode}")
    public List<SermonPreaching> getSermonPreachingByLanguage(@PathVariable String languageCode) {
        return this.sermonPreachingService.getByLanguageCode(languageCode);
    }

    @GetMapping(value = "/language/{languageCode}/{id}")
    public List<SermonPreaching>getSermonPreachingByIdAndLanguageCode(@PathVariable  String languageCode, @PathVariable Integer id){
        return this.sermonPreachingService.getByIdAndLanguageCode(languageCode, id);
    }

    @GetMapping(value = "/book/{bookNumber}")
    List<SermonPreaching> getSermonPreachingByBookNumber(@PathVariable Integer bookNumber) {
        return this.sermonPreachingService.getByBookNumber(bookNumber);
    }

    @GetMapping(value = "/book/{bookNumber}/{chapterNumber}")
    List<SermonPreaching> getSermonPreachingByBookAndChapterNumber(@PathVariable Integer bookNumber, @PathVariable Integer chapterNumber) {
        return this.sermonPreachingService.getByBookAndChapterNumber(bookNumber, chapterNumber);
    }

    @GetMapping(value = "/title/{title}")
    List<SermonPreaching> getSermonPreachingByTitle(@PathVariable String title) {
        return this.sermonPreachingService.getByTitle(title);
    }

    @PostMapping(value = "{id}/verses")
    SermonPreaching saveSermonPreachingVerse(@PathVariable Integer id, @RequestBody SermonPreachingVerse sermonPreachingVerse) throws NotFoundException {
        this.sermonPreachingVerseService.save(sermonPreachingVerse);
        return this.sermonPreachingService.getById(id);
    }

}
