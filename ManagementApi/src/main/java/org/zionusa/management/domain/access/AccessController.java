package org.zionusa.management.domain.access;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/accesses")
@Api(value = "User Access Levels")
public class AccessController extends BaseController<Access, Integer> {

    @Autowired
    public AccessController(AccessService service) {
        super(service);
    }

}
