package org.zionusa.admin.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.Subject;
import org.zionusa.admin.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping()
    List<Subject> getAll() {
        return subjectService.getAll(null);
    }

    @GetMapping(value = "/{id}")
    Subject getById(@PathVariable Integer id) throws NotFoundException {
        return subjectService.getById(id);
    }

    @PostMapping()
    Subject save(@RequestBody Subject subject) {
        return subjectService.save(subject);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable Integer id) throws NotFoundException {
        subjectService.delete(id);
    }

}
