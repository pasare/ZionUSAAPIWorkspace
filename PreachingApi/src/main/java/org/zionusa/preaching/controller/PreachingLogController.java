package org.zionusa.preaching.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.controller.BaseController;
import org.zionusa.preaching.domain.PreachingLog;
import org.zionusa.preaching.service.PreachingLogService;

import java.util.List;

@RestController
@RequestMapping("/preaching-logs")
public class PreachingLogController extends BaseController<PreachingLog> {

    private final PreachingLogService preachingLogService;

    @Autowired
    public PreachingLogController(PreachingLogService preachingLogService) {
        super(preachingLogService);
        this.preachingLogService = preachingLogService;
    }

    @GetMapping(value = "/user/{userId}")
    List<PreachingLog> getPreachingLogsByUserId(@PathVariable Integer userId) {
        return preachingLogService.getByUserId(userId);
    }

    @GetMapping(value = "/{userId}/{startDate}/{endDate}")
    List<PreachingLog> getPreachingLogsByUserIdAndDate(@PathVariable Integer userId,
                                          @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String startDate,
                                          @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") String endDate){
        return preachingLogService.getPreachingLogsByUserIdAndDate(userId, startDate, endDate);
    }

    @DeleteMapping(value = "/{preachingLogId}")
    void delete(@PathVariable Integer preachingLogId) throws NotFoundException {
        preachingLogService.delete(preachingLogId);
    }
}
