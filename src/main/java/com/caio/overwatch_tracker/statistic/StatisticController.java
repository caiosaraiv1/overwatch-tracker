package com.caio.overwatch_tracker.statistic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/statistics/heroes/{heroId}")
    public ResponseEntity<StatisticResponse> getStatistics(@PathVariable("heroId") Long heroId) {
        StatisticResponse statisticResponse = statisticService.getStatistics(heroId);
        return ResponseEntity.ok(statisticResponse);
    }
}
