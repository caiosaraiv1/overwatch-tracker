package com.caio.overwatch_tracker.performance;

import com.caio.overwatch_tracker.hero.Hero;
import com.caio.overwatch_tracker.hero.HeroRepository;
import com.caio.overwatch_tracker.match.Match;
import com.caio.overwatch_tracker.match.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final MatchRepository matchRepository;
    private final HeroRepository heroRepository;

    public PerformanceService(PerformanceRepository performanceRepository, MatchRepository matchRepository, HeroRepository heroRepository) {
        this.performanceRepository = performanceRepository;
        this.matchRepository = matchRepository;
        this.heroRepository = heroRepository;
    }

    public Performance addPerformance(Long matchId, PerformanceRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match ID not found."));

        Hero hero = heroRepository.findById(request.getHeroId())
                .orElseThrow(() -> new RuntimeException("Hero ID not found."));

        Performance performance = new Performance();

        performance.setMatch(match);
        performance.setHero(hero);

        performance.setHeroOrder(request.getHeroOrder());
        performance.setTimePlayed(request.getTimePlayed());
        performance.setEliminations(request.getEliminations());
        performance.setAssists(request.getAssists());
        performance.setDeaths(request.getDeaths());
        performance.setDamageDealt(request.getDamageDealt());
        performance.setHealingDone(request.getHealingDone());
        performance.setDamageMitigated(request.getDamageMitigated());

        performance = performanceRepository.save(performance);

        return performance;
    }

    public Performance updatePerformance(Long matchId, Long performanceId, PerformanceRequest request) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("Performance ID not found."));

        if (!performance.getMatch().getId().equals(matchId)) {
            throw new RuntimeException("Performance " + performanceId + " does not belong to Match " + matchId);
        }

        Hero hero = heroRepository.findById(request.getHeroId())
                .orElseThrow(() -> new RuntimeException("Hero ID not found."));

        performance.setHero(hero);

        performance.setHeroOrder(request.getHeroOrder());
        performance.setTimePlayed(request.getTimePlayed());
        performance.setEliminations(request.getEliminations());
        performance.setAssists(request.getAssists());
        performance.setDeaths(request.getDeaths());
        performance.setDamageDealt(request.getDamageDealt());
        performance.setHealingDone(request.getHealingDone());
        performance.setDamageMitigated(request.getDamageMitigated());

        performance = performanceRepository.save(performance);

        return performance;
    }

    public void deletePerformance(Long matchId, Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("Performance ID not found."));

        if (!performance.getMatch().getId().equals(matchId)) {
            throw new RuntimeException("Performance " + performanceId + " does not belong to Match " + matchId);
        }

        performanceRepository.delete(performance);
    }

    public List<Performance> findByMatchId(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new RuntimeException("Match ID not found.");
        }

        return performanceRepository.findByMatchId(matchId);
    }

}
