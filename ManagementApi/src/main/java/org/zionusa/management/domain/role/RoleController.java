package org.zionusa.management.domain.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/roles")
public class RoleController extends BaseController<Role, Integer> {

    @Autowired
    public RoleController(RoleService roleService) {
        super(roleService);
    }

}
