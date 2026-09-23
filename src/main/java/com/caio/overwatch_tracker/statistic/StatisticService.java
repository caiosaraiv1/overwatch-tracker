package com.caio.overwatch_tracker.statistic;

import com.caio.overwatch_tracker.hero.Hero;
import com.caio.overwatch_tracker.hero.HeroRepository;
import com.caio.overwatch_tracker.match.Result;
import com.caio.overwatch_tracker.performance.Performance;
import com.caio.overwatch_tracker.performance.PerformanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticService {

    private final PerformanceRepository performanceRepository;
    private final HeroRepository heroRepository;

    public StatisticService(PerformanceRepository performanceRepository, HeroRepository heroRepository) {
        this.performanceRepository = performanceRepository;
        this.heroRepository = heroRepository;
    }

    public StatisticResponse getStatistics(Long heroId) {
        StatisticResponse statisticResponse = new StatisticResponse();

        Hero hero = heroRepository.findById(heroId)
                .orElseThrow(() -> new RuntimeException("Hero ID not found."));

        List<Performance> heroPerformances = performanceRepository.findByHeroId(heroId);

        int totalWins = (int) heroPerformances
                .stream()
                .filter(hp -> hp.getMatch().getMatchResult() == Result.VICTORY)
                .count();

        int totalGamesPlayed = heroPerformances.size();

        int totalDefeats = (int) heroPerformances
                .stream()
                .filter(hp -> hp.getMatch().getMatchResult() == Result.DEFEAT)
                .count();

        int totalDraws = (int) heroPerformances
                .stream()
                .filter(hp -> hp.getMatch().getMatchResult() == Result.DRAW)
                .count();

        double averageEliminations = heroPerformances
                .stream()
                .mapToInt(hp -> hp.getEliminations())
                .average()
                .orElse(0.0);

        double averageAssists = heroPerformances
                .stream()
                .mapToInt(hp -> hp.getAssists())
                .average()
                .orElse(0.0);

        double averageDeaths = heroPerformances
                .stream()
                .mapToInt(hp -> hp.getDeaths())
                .average()
                .orElse(0.0);

        double averageDamageDealt = heroPerformances
                .stream()
                .mapToInt(hp -> hp.getDamageDealt())
                .average()
                .orElse(0.0);

        double averageHealingDone = heroPerformances
                .stream()
                .mapToInt(hp -> hp.getHealingDone())
                .average()
                .orElse(0.0);

        double averageDamageMitigated = heroPerformances
                .stream()
                .mapToInt(hp -> hp.getDamageMitigated())
                .average()
                .orElse(0.0);

        statisticResponse.setHeroName(hero.getName());

        if (totalGamesPlayed == 0) {
            statisticResponse.setWinRate(0.0);
        } else {
            statisticResponse.setWinRate((double) totalWins / totalGamesPlayed);
        }

        statisticResponse.setTotalGamesPlayed(totalGamesPlayed);
        statisticResponse.setTotalWins(totalWins);
        statisticResponse.setTotalDefeats(totalDefeats);
        statisticResponse.setTotalDraws(totalDraws);

        statisticResponse.setAvgEliminations(averageEliminations);
        statisticResponse.setAvgAssists(averageAssists);
        statisticResponse.setAvgDeaths(averageDeaths);
        statisticResponse.setAvgDamageDealt(averageDamageDealt);
        statisticResponse.setAvgHealingDone(averageHealingDone);
        statisticResponse.setAvgDamageMitigated(averageDamageMitigated);

        return statisticResponse;
    }
}
