package com.caio.overwatch_tracker.match;

import com.caio.overwatch_tracker.hero.Hero;
import com.caio.overwatch_tracker.hero.HeroRepository;
import com.caio.overwatch_tracker.performance.Performance;
import com.caio.overwatch_tracker.performance.PerformanceRepository;
import com.caio.overwatch_tracker.performance.PerformanceRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final HeroRepository heroRepository;
    private final PerformanceRepository performanceRepository;

    public MatchService(MatchRepository matchRepository, HeroRepository heroRepository, PerformanceRepository performanceRepository) {
        this.matchRepository = matchRepository;
        this.heroRepository = heroRepository;
        this.performanceRepository = performanceRepository;
    }

    @Transactional
    public Match createMatch(MatchRequest request) {
        Match match = new Match();

        match.setDateTime(request.getDateTime());
        match.setMatchMap(request.getMatchMap());
        match.setMapType(request.getMapType());
        match.setMatchMode(request.getMatchMode());
        match.setMatchResult(request.getMatchResult());
        match.setMatchScore(request.getMatchScore());
        match.setTotalDuration(request.getTotalDuration());

        match = matchRepository.save(match);

        for (PerformanceRequest pr : request.getPerformanceRequestList()) {
            Hero hero = heroRepository.findById(pr.getHeroId())
                    .orElseThrow(() -> new RuntimeException("Hero ID not found."));

            Performance performance = new Performance();

            performance.setMatch(match);
            performance.setHero(hero);

            performance.setHeroOrder(pr.getHeroOrder());
            performance.setTimePlayed(pr.getTimePlayed());
            performance.setEliminations(pr.getEliminations());
            performance.setAssists(pr.getAssists());
            performance.setDeaths(pr.getDeaths());
            performance.setDamageDealt(pr.getDamageDealt());
            performance.setHealingDone(pr.getHealingDone());
            performance.setDamageMitigated(pr.getDamageMitigated());

            performance = performanceRepository.save(performance);
        }

        return match;
    }

    public List<Match> findAll() {
        return matchRepository.findAll();
    }
}
