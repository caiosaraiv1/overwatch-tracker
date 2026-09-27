package com.caio.overwatch_tracker.performance;

public class PerformanceNotFoundException extends RuntimeException {

    private final Long performanceId;

    public PerformanceNotFoundException(Long performanceId) {
        super("Performance ID " + performanceId + " not found.");
        this.performanceId = performanceId;
    }

    public Long getPerformanceId() {
        return performanceId;
    }
}
