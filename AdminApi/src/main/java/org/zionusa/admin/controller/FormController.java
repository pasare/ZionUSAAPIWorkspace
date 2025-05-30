package org.zionusa.admin.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zionusa.admin.domain.Form;
import org.zionusa.admin.service.FormService;

import java.util.List;

@RestController
@RequestMapping("/forms")
public class FormController {

    private final FormService formService;

    @Autowired
    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping()
    public List<Form> getAll() {
        return formService.getAll(null);
    }

    @GetMapping(value = "/{id}")
    public Form getById(@PathVariable Integer id) throws NotFoundException {
        return formService.getById(id);
    }

    @PostMapping()
    public Form save(@RequestBody Form form) {
        return formService.save(form);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Integer id) throws NotFoundException {
        formService.delete(id);
    }
}
