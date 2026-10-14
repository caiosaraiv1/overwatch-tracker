package com.caio.overwatch_tracker.statistic;

import com.caio.overwatch_tracker.hero.Hero;
import com.caio.overwatch_tracker.hero.HeroNotFoundException;
import com.caio.overwatch_tracker.hero.HeroRepository;
import com.caio.overwatch_tracker.match.Match;
import com.caio.overwatch_tracker.match.Result;
import com.caio.overwatch_tracker.performance.Performance;
import com.caio.overwatch_tracker.performance.PerformanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticServiceTest {

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private HeroRepository heroRepository;

    @InjectMocks
    private StatisticService statisticService;

    // ===================== getStatistics =====================

    @Test
    public void getStatistics_shouldThrowHeroNotFound() {
        Long heroId = 10L;

        when(heroRepository.findById(heroId)).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class,
                () -> statisticService.getStatistics(heroId));

        verify(performanceRepository, never()).findByHeroId(any());
    }

    @Test
    public void getStatistics_shouldReturnZeroedStatistics_whenHeroHasNoPerformances() {
        Hero hero = createHero(10L);
        List<Performance> heroPerformance = List.of();

        when(heroRepository.findById(hero.getId())).thenReturn(Optional.of(hero));
        when(performanceRepository.findByHeroId(hero.getId())).thenReturn(heroPerformance);

        StatisticResponse result = statisticService.getStatistics(hero.getId());

        assertEquals(hero.getName(), result.getHeroName());
        assertEquals(0.0, result.getWinRate());
        assertEquals(0, result.getTotalGamesPlayed());
        assertEquals(0, result.getTotalWins());
        assertEquals(0, result.getTotalDefeats());
        assertEquals(0, result.getTotalDraws());
        assertEquals(0.0, result.getAvgEliminations());
        assertEquals(0.0, result.getAvgAssists());
        assertEquals(0.0, result.getAvgDeaths());
        assertEquals(0.0, result.getAvgDamageDealt());
        assertEquals(0.0, result.getAvgHealingDone());
        assertEquals(0.0, result.getAvgDamageMitigated());

        verify(heroRepository).findById(hero.getId());
        verify(performanceRepository).findByHeroId(hero.getId());
    }

    @Test
    public void getStatistics_shouldCalculateStatistics_whenHeroHasPerformances() {
        Hero hero = createHero(10L);
        List<Performance> heroPerformances = List.of(
                createPerformanceWithStats(Result.VICTORY, 10,  5, 2, 1000, 4000, 100),
                createPerformanceWithStats(Result.VICTORY, 20, 15, 4, 2000, 8000, 300),
                createPerformanceWithStats(Result.DEFEAT,  30, 10, 6, 3000,    0, 200),
                createPerformanceWithStats(Result.DRAW,    40, 10, 0, 6000, 4000, 200)
        );

        when(heroRepository.findById(hero.getId())).thenReturn(Optional.of(hero));
        when(performanceRepository.findByHeroId(hero.getId())).thenReturn(heroPerformances);

        StatisticResponse result = statisticService.getStatistics(hero.getId());

        assertEquals(hero.getName(), result.getHeroName());
        assertEquals(0.5, result.getWinRate());
        assertEquals(4, result.getTotalGamesPlayed());
        assertEquals(2, result.getTotalWins());
        assertEquals(1, result.getTotalDefeats());
        assertEquals(1, result.getTotalDraws());
        assertEquals(25.0, result.getAvgEliminations());
        assertEquals(10.0, result.getAvgAssists());
        assertEquals(3.0, result.getAvgDeaths());
        assertEquals(3000.0, result.getAvgDamageDealt());
        assertEquals(4000.0, result.getAvgHealingDone());
        assertEquals(200.0, result.getAvgDamageMitigated());

        verify(heroRepository).findById(hero.getId());
        verify(performanceRepository).findByHeroId(hero.getId());
    }

    // ===================== métodos auxiliares =====================

    private Hero createHero(Long heroId) {
        Hero hero = new Hero();
        hero.setId(heroId);
        hero.setName("Ana");
        return hero;
    }

    private Performance createPerformanceWithStats(Result result,
                                                   int eliminations, int assists, int deaths,
                                                   int damageDealt, int healingDone, int damageMitigated) {
        Match match = new Match();
        match.setMatchResult(result);

        Performance performance = new Performance();
        performance.setMatch(match);
        performance.setEliminations(eliminations);
        performance.setAssists(assists);
        performance.setDeaths(deaths);
        performance.setDamageDealt(damageDealt);
        performance.setHealingDone(healingDone);
        performance.setDamageMitigated(damageMitigated);
        return performance;
    }
}
