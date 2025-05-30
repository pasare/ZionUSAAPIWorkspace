package org.zionusa.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.management.domain.UserPermissionForManagement;
import org.zionusa.management.service.UserPermissionService;

import java.util.List;

@RestController
@RequestMapping("/user-permissions")
public class UserPermissionController extends BaseController<UserPermissionForManagement, Integer> {
    private final UserPermissionService userPermissionService;

    public UserPermissionController(UserPermissionService userPermissionService) {
        super(userPermissionService);
        this.userPermissionService = userPermissionService;
    }

    @GetMapping(value = "/user/{id}")
    public List<String> getByUserId(@PathVariable Integer id) {
        return userPermissionService.getByUserId(id);
    }

}
