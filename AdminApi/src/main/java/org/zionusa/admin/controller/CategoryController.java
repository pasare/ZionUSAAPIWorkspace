package org.zionusa.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.Category;
import org.zionusa.admin.service.CategoryService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/categories")
public class CategoryController extends BaseController<Category, Integer> {
    @Autowired
    public CategoryController(CategoryService service) {
        super(service);
    }
}
