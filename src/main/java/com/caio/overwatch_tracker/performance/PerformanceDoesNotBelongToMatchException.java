package com.caio.overwatch_tracker.performance;

public class PerformanceDoesNotBelongToMatchException extends RuntimeException {

    private final Long performanceId;
    private final Long matchId;

    public PerformanceDoesNotBelongToMatchException(Long performanceId, Long matchId) {
        super("Performance " + performanceId + " does not belong to Match " + matchId);
        this.performanceId = performanceId;
        this.matchId = matchId;
    }
}
