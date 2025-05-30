package org.zionusa.preaching.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zionusa.base.controller.BaseController;
import org.zionusa.preaching.domain.PreachingMaterial;
import org.zionusa.preaching.service.PreachingMaterialService;

import java.util.List;

@RestController
@RequestMapping("/preaching-materials")
public class PreachingMaterialController extends BaseController<PreachingMaterial> {

    private final PreachingMaterialService preachingMaterialService;

    @Autowired
    public PreachingMaterialController(PreachingMaterialService preachingMaterialService) {
        super(preachingMaterialService);
        System.out.println(preachingMaterialService);
        this.preachingMaterialService = preachingMaterialService;

    }

    @PostMapping(value = "/upload/{id}")
    public PreachingMaterial upload(@PathVariable Integer id, @RequestParam(value = "file") MultipartFile file) throws NotFoundException {
        return preachingMaterialService.upload(id, file);
    }


    @GetMapping(value = "/language")
    public  List<PreachingMaterial> getAll(@RequestParam(required = false) String languageCode){
        if (languageCode == null){
            languageCode = "en";
        }
        return this.preachingMaterialService.getByLanguageCode(languageCode);
    }


    @GetMapping(value = "/language/{languageCode}")
    List<PreachingMaterial> getPreachingMaterialsByLanguageCode(@PathVariable String languageCode) {
        return this.preachingMaterialService.getByLanguageCode(languageCode);
    }

    @GetMapping(value = "/language/{languageCode}/{id}")
    List<PreachingMaterial> getPreachingMaterialsByLanguageCodeAndId(@PathVariable String languageCode, @PathVariable Integer id){
        return this.preachingMaterialService.getByLanguageCodeAndId(languageCode, id);
    }
}
