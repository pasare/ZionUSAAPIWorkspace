package org.zionusa.admin.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.MothersTeaching;
import org.zionusa.admin.service.MothersTeachingService;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/mothers-teachings")
@Api(value = "MothersTeachings")

public class MothersTeachingController {
    private final MothersTeachingService mothersTeachingService;


    public MothersTeachingController(MothersTeachingService mothersTeachingService) {
        this.mothersTeachingService = mothersTeachingService;
    }

    @GetMapping()
    public List<MothersTeaching> getAll() {
        return mothersTeachingService.getAll();
    }

    @PutMapping("/schedule/{number}")
    public void scheduleMothersTeachingNotification(@PathVariable Integer number) {
        mothersTeachingService.getMothersTeachingToSend(number);
    }

    @GetMapping("/{id}")
    public MothersTeaching getMothersTeaching(@PathVariable Integer id) {
        return mothersTeachingService.getMothersTeachingById(id);
    }

    @PostMapping()
    public MothersTeaching saveMothersTeaching(@RequestBody MothersTeaching mothersTeaching) {
        return mothersTeachingService.saveMothersTeaching(mothersTeaching);
    }

    @PutMapping("/{id}")
    public MothersTeaching updateMothersTeaching(@RequestBody MothersTeaching mothersTeaching) {
        return mothersTeachingService.saveMothersTeaching(mothersTeaching);
    }

    @DeleteMapping("/{id}")
    public void deleteMothersTeaching(@PathVariable Integer id) throws NotFoundException {
        mothersTeachingService.deleteMothersTeaching(id);
    }
}
