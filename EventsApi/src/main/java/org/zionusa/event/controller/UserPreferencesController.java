package org.zionusa.event.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.UserPreference;
import org.zionusa.event.service.UserPreferencesService;

import java.util.List;

@RestController
@RequestMapping("/user-preferences")
public class UserPreferencesController extends BaseController<UserPreference, Integer> {

    private final UserPreferencesService userPreferencesService;

    public UserPreferencesController(UserPreferencesService userPreferencesService) {
        super(userPreferencesService);
        this.userPreferencesService = userPreferencesService;
    }

    @GetMapping(value = "/user/{id}")
    public List<UserPreference> getByUserId(@PathVariable Integer id) {
        return userPreferencesService.getByUserId(id);
    }

}
