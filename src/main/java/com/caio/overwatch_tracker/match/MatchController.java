package com.caio.overwatch_tracker.match;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @ApiResponse(responseCode = "201", description = "Match created")
    @PostMapping("/matches")
    public ResponseEntity<Match> createMatch(@Valid @RequestBody MatchRequest matchRequest) {
        Match save = matchService.createMatch(matchRequest);
        return ResponseEntity.status(201).body(save);
    }

    @GetMapping("/matches")
    public ResponseEntity<List<Match>> getMatches() {
        List<Match> matches = matchService.findAll();
        return ResponseEntity.ok(matches);
    }

    @PutMapping("/matches/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable("id") Long matchId, @Valid @RequestBody MatchRequest request) {
        Match save = matchService.updateMatch(matchId, request);
        return ResponseEntity.status(200).body(save);
    }
}
