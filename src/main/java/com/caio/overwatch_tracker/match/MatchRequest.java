package com.caio.overwatch_tracker.match;

import com.caio.overwatch_tracker.performance.PerformanceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class MatchRequest {

    @NotNull
    @PastOrPresent
    private LocalDateTime dateTime;

    @NotBlank
    private String matchMap;

    @NotNull
    private MapType mapType;

    @NotNull
    private MatchMode matchMode;

    @NotNull
    private Result matchResult;

    @NotBlank
    @Pattern(regexp = "^\\d+ vs \\d+$")
    private String matchScore;

    @NotNull
    @DurationMin(seconds = 1)
    private Duration totalDuration;

    @NotNull
    @Valid
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
