package org.zionusa.biblestudy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.biblestudy.domain.Reformer;
import org.zionusa.biblestudy.domain.ReformerReport;
import org.zionusa.biblestudy.service.ReformerService;

import java.util.List;

@RestController
@RequestMapping("/reformers")
public class ReformerController extends BaseController<Reformer, Integer> {

    private final ReformerService reformerService;

    @Autowired
    public ReformerController(ReformerService reformerService) {
        super(reformerService);
        this.reformerService = reformerService;
    }

    @GetMapping(value = "/user/{userId}")
    public List<Reformer> getAllReformerByUserId(@PathVariable Integer userId) {
        return reformerService.getByUser(userId);
    }

    @GetMapping(value = "/report/team/{id}/{startDate}/{endDate}")
    public ReformerReport getTeamReformerReport(@PathVariable Integer id, @PathVariable String startDate, @PathVariable String endDate) {
        return reformerService.getTeamReformerReport(id, startDate, endDate);
    }

    @GetMapping(value = "/report/group/{id}/{startDate}/{endDate}")
    public ReformerReport getGroupReformerReport(@PathVariable Integer id, @PathVariable String startDate, @PathVariable String endDate) {
        return reformerService.getGroupReformerReport(id, startDate, endDate);
    }

    @GetMapping(value = "/report/church/{id}/{startDate}/{endDate}")
    public ReformerReport getChurchReformerReport(@PathVariable Integer id, @PathVariable String startDate, @PathVariable String endDate) {
        return reformerService.getChurchReformerReport(id, startDate, endDate);
    }

    @GetMapping(value = "/report/east-coast/{startDate}/{endDate}")
    public ReformerReport getEastReformerReport(@PathVariable String startDate, @PathVariable String endDate) {
        return reformerService.getEastCoastReformerReport(startDate, endDate);
    }
}
