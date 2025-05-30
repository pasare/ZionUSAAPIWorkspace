package org.zionusa.management.domain.branchtype;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.base.enums.EBranchType;

@RestController
@RequestMapping("/branch-types")
@Api(value = "Branch Types")
public class BranchTypeController extends BaseController<BranchType, EBranchType> {
    @Autowired
    public BranchTypeController(BranchTypeService service) {
        super(service);
    }
}
