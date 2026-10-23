package com.cricket.controller;

import com.cricket.service.RealtimeNotificationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/stream")
public class StreamController {

    private final RealtimeNotificationService notificationService;

    public StreamController(RealtimeNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLiveUpdates() {
        return notificationService.createSseEmitter();
    }
}
