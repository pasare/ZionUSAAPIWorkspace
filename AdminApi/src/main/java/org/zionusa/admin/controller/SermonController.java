package org.zionusa.admin.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.Sermon;
import org.zionusa.admin.service.SermonService;

import java.util.List;

@RestController
@RequestMapping("/sermons")
public class SermonController {

    private final SermonService sermonService;

    @Autowired
    public SermonController(SermonService sermonService) {
        this.sermonService = sermonService;
    }

    @GetMapping()
    List<Sermon> getAll() {
        return sermonService.getAll(null);
    }

    @GetMapping(value = "/{id}")
    Sermon getById(@PathVariable Integer id) throws NotFoundException {
        return sermonService.getById(id);
    }

    @PostMapping()
    Sermon save(@RequestBody Sermon sermon) {
        return sermonService.save(sermon);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable Integer id) throws NotFoundException {
        sermonService.delete(id);
    }

}
