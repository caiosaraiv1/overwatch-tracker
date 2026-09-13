package com.caio.overwatch_tracker.match;

import com.caio.overwatch_tracker.performance.PerformanceRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class MatchRequest {

    private LocalDateTime dateTime;
    private String matchMap;
    private MapType mapType;
    private MatchMode matchMode;
    private Result matchResult;
    private String matchScore;
    private Duration totalDuration;
    private List<PerformanceRequest> performanceRequestList;

    public MatchRequest() {
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getMatchMap() {
        return matchMap;
    }

    public void setMatchMap(String matchMap) {
        this.matchMap = matchMap;
    }

    public MapType getMapType() {
        return mapType;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(MatchMode matchMode) {
        this.matchMode = matchMode;
    }

    public Result getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(Result matchResult) {
        this.matchResult = matchResult;
    }

    public String getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(String matchScore) {
        this.matchScore = matchScore;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<PerformanceRequest> getPerformanceRequestList() {
        return performanceRequestList;
    }

    public void setPerformanceRequestList(List<PerformanceRequest> performanceRequestList) {
        this.performanceRequestList = performanceRequestList;
    }
}
