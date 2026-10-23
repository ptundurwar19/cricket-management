package com.cricket.service;

import com.cricket.dto.LiveScoreUpdateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RealtimeNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(RealtimeNotificationService.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final List<SseEmitter> sseEmitters = new CopyOnWriteArrayList<>();

    public RealtimeNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastLiveScore(LiveScoreUpdateDTO update) {
        try {
            // 1. Broadcast to global topic
            messagingTemplate.convertAndSend("/topic/matches", update);

            // 2. Broadcast to match-specific topic
            if (update.getMatchId() != null) {
                messagingTemplate.convertAndSend("/topic/matches/" + update.getMatchId(), update);
            }
        } catch (Exception e) {
            logger.warn("Failed to send WebSocket message: {}", e.getMessage());
        }

        // 3. Broadcast to all active SSE subscribers
        broadcastSse(update);
    }

    public SseEmitter createSseEmitter() {
        SseEmitter emitter = new SseEmitter(180_000L); // 3 minutes timeout
        sseEmitters.add(emitter);

        emitter.onCompletion(() -> sseEmitters.remove(emitter));
        emitter.onTimeout(() -> sseEmitters.remove(emitter));
        emitter.onError((e) -> sseEmitters.remove(emitter));

        return emitter;
    }

    private void broadcastSse(LiveScoreUpdateDTO update) {
        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();
        for (SseEmitter emitter : sseEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("score-update")
                        .data(update));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        sseEmitters.removeAll(deadEmitters);
    }
}
