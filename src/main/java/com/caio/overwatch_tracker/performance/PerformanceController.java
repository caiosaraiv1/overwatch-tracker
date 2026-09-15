package com.caio.overwatch_tracker.performance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping("/matches/{id}/performances")
    public ResponseEntity<Performance> addPerformance(@RequestBody PerformanceRequest performanceRequest, @PathVariable("id") Long matchId) {
        Performance save = performanceService.addPerformance(matchId, performanceRequest);
        return ResponseEntity.status(201).body(save);
    }

    @PatchMapping("/matches/{id}/performances/{performanceId}")
    public ResponseEntity<Performance> updatePerformance(@RequestBody PerformanceRequest performanceRequest, @PathVariable("id") Long matchId, @PathVariable("performanceId") Long performanceId) {
        Performance save = performanceService.updatePerformance(matchId, performanceId, performanceRequest);
        return ResponseEntity.status(200).body(save);
    }

    @DeleteMapping("/matches/{id}/performances/{performanceId}")
    public ResponseEntity<Void> deletePerformance(@PathVariable("id") Long matchId, @PathVariable("performanceId") Long performanceId) {
        performanceService.deletePerformance(matchId, performanceId);
        return ResponseEntity.noContent().build();
    }
}
