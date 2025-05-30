package org.zionusa.management.domain.usertype;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.base.enums.EUserType;

@RestController
@RequestMapping("/user-types")
@Api(value = "User Types")
public class UserTypeController extends BaseController<UserType, EUserType> {
    @Autowired
    public UserTypeController(UserTypeService service) {
        super(service);
    }
}
