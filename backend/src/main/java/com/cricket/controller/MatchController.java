package com.cricket.controller;

import com.cricket.dto.CreateMatchRequest;
import com.cricket.dto.LiveScoreUpdateDTO;
import com.cricket.dto.ManualBallRequest;
import com.cricket.model.BallEvent;
import com.cricket.model.CricketMatch;
import com.cricket.model.MatchStatus;
import com.cricket.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<List<CricketMatch>> getAllMatches(@RequestParam(required = false) MatchStatus status) {
        return ResponseEntity.ok(matchService.getAllMatches(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CricketMatch> getMatchById(@PathVariable Long id) {
        CricketMatch match = matchService.getMatchById(id);
        if (match == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(match);
    }

    @PostMapping
    public ResponseEntity<CricketMatch> createMatch(@RequestBody CreateMatchRequest request) {
        try {
            CricketMatch match = matchService.createMatch(request);
            return ResponseEntity.ok(match);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Map<String, Object>> startSimulation(@PathVariable Long id,
                                                               @RequestParam(required = false, defaultValue = "1500") Integer speedMs) {
        matchService.startSimulation(id, speedMs);
        return ResponseEntity.ok(Map.of("message", "Simulation started", "matchId", id, "speedMs", speedMs));
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<Map<String, Object>> pauseSimulation(@PathVariable Long id) {
        matchService.pauseSimulation(id);
        return ResponseEntity.ok(Map.of("message", "Simulation paused", "matchId", id));
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<Map<String, Object>> resumeSimulation(@PathVariable Long id) {
        matchService.resumeSimulation(id);
        return ResponseEntity.ok(Map.of("message", "Simulation resumed", "matchId", id));
    }

    @PostMapping("/{id}/reset")
    public ResponseEntity<Map<String, Object>> resetMatch(@PathVariable Long id) {
        matchService.resetMatch(id);
        return ResponseEntity.ok(Map.of("message", "Match reset to initial state", "matchId", id));
    }

    @PostMapping("/{id}/ball")
    public ResponseEntity<LiveScoreUpdateDTO> processManualBall(@PathVariable Long id,
                                                                @RequestBody ManualBallRequest request) {
        LiveScoreUpdateDTO result = matchService.processManualBall(id, request);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/commentary")
    public ResponseEntity<List<BallEvent>> getCommentary(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getCommentary(id));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<Map<String, Object>> getLeaderboard() {
        return ResponseEntity.ok(matchService.getLeaderboard());
    }
}
