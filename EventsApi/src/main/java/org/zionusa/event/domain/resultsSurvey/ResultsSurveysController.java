package org.zionusa.event.domain.resultsSurvey;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/results-surveys")
public class ResultsSurveysController extends BaseController<ResultsSurvey, Integer> {
    public final ResultsSurveysService resultsSurveysService;

    public ResultsSurveysController(ResultsSurveysService resultsSurveysService) {
        super(resultsSurveysService);
        this.resultsSurveysService = resultsSurveysService;
    }

    @GetMapping(value = "/{id}/display")
    public ResultsSurveyView getDisplayById(@PathVariable Integer id) {
        return resultsSurveysService.getDisplayById(id);
    }
}
