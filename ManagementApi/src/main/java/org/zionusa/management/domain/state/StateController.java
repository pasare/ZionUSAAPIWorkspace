package org.zionusa.management.domain.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/states")
public class StateController extends BaseController<State, Integer> {

    @Autowired
    public StateController(StateService stateService) {
        super(stateService);
    }

}
