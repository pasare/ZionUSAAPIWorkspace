package org.zionusa.event.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;
import org.zionusa.event.domain.EventMedia;
import org.zionusa.event.service.EventMediaService;

import java.util.List;

@RestController
@RequestMapping("/event-media")
public class EventMediaController extends BaseController<EventMedia, Integer> {
    private final EventMediaService eventMediaService;

    public EventMediaController(EventMediaService eventMediaService) {
        super(eventMediaService);
        this.eventMediaService = eventMediaService;
    }

    @GetMapping(value = "/event-proposal/{eventProposalId}")
    public List<EventMedia> findAllByEventProposalId(@PathVariable Integer eventProposalId) {
        return eventMediaService.findAllByEventProposalId(eventProposalId);
    }

    @GetMapping(value = "/results-survey/{resultsSurveyId}")
    public List<EventMedia> findAllByResultsSurveyId(@PathVariable Integer resultsSurveyId) {
        return eventMediaService.findAllByResultsSurveyId(resultsSurveyId);
    }
}
