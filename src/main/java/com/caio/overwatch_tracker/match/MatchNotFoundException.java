package com.caio.overwatch_tracker.match;

public class MatchNotFoundException extends RuntimeException{

    private final Long matchId;

    public MatchNotFoundException(Long matchId) {
        super("Match ID " + matchId + " not found.");
        this.matchId = matchId;
    }

    public Long getMatchId() {
        return matchId;
    }
}
