package com.brokers.digitalbanking.query.controllers;

import com.brokers.digitalbanking.query.service.ReplayEventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/query/accounts")
public class ReplayEventsController {
    private ReplayEventService replayEventService;

    public ReplayEventsController(ReplayEventService replayEventService) {
        this.replayEventService = replayEventService;
    }

    @GetMapping("/replayEvents")
    public String ReplayEvents(){
        replayEventService.replay();
        return "success Playing events";
    }
}
