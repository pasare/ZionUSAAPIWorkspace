package org.zionusa.management.domain.specialDay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

import java.util.List;


@RestController
@RequestMapping("/special-days")
public class SpecialDayController extends BaseController<SpecialDay, Integer> {
    SpecialDayService service;

    @Autowired
    public SpecialDayController(SpecialDayService service) {
        super(service);
    }

    @GetMapping("/{startDate}/{endDate}")
    public List<SpecialDay> getSpecialDaysBetweenDates(@PathVariable String startDate, @PathVariable String endDate) {
        return service.getAllBetweenDates(startDate, endDate);
    }
}

