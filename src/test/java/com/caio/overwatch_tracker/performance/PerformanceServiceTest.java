package com.caio.overwatch_tracker.performance;

import com.caio.overwatch_tracker.hero.Hero;
import com.caio.overwatch_tracker.hero.HeroNotFoundException;
import com.caio.overwatch_tracker.hero.HeroRepository;
import com.caio.overwatch_tracker.match.Match;
import com.caio.overwatch_tracker.match.MatchNotFoundException;
import com.caio.overwatch_tracker.match.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceTest {

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private HeroRepository heroRepository;

    @InjectMocks
    private PerformanceService performanceService;

    // ===================== deletePerformance =====================

    @Test
    public void deletePerformance_shouldDeletePerformance() {
        Match match = createMatch(1L);
        Performance performance = createPerformance(2L, match);

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));

        performanceService.deletePerformance(match.getId(), performance.getId());

        verify(performanceRepository).delete(performance);
    }

    @Test
    public void deletePerformance_shouldThrowPerformanceNotFound() {
        Long matchId = 1L;
        Long performanceId = 2L;

        when(performanceRepository.findById(performanceId)).thenReturn(Optional.empty());

        assertThrows(PerformanceNotFoundException.class,
                () -> performanceService.deletePerformance(matchId, performanceId));

        verify(performanceRepository, never()).delete(any());
    }

    @Test
    public void deletePerformance_shouldThrowPerformanceDoesNotBelongToMatch() {
        Match match = createMatch(1L);
        Performance performance = createPerformance(2L, match);
        Long incorrectMatchId = 3L;

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));

        assertThrows(PerformanceDoesNotBelongToMatchException.class,
                () -> performanceService.deletePerformance(incorrectMatchId, performance.getId()));

        verify(performanceRepository, never()).delete(any());
    }

    // ===================== updatePerformance =====================

    @Test
    public void updatePerformance_shouldUpdatePerformance() {
        Match match = createMatch(1L);
        Hero oldHero = createHero(10L);
        Hero newHero = createHero(11L);

        Performance performance = createPerformance(2L, match);
        performance.setHero(oldHero);

        PerformanceRequest performanceRequest = createPerformanceRequest(newHero.getId());

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));
        when(heroRepository.findById(newHero.getId())).thenReturn(Optional.of(newHero));
        when(performanceRepository.save(performance)).thenReturn(performance);

        Performance result = performanceService.updatePerformance(match.getId(), performance.getId(), performanceRequest);

        assertEquals(newHero, result.getHero());

        verify(performanceRepository).save(performance);
    }

    @Test
    public void updatePerformance_shouldThrowPerformanceNotFound() {
        Long matchId = 1L;
        Long performanceId = 2L;
        PerformanceRequest performanceRequest = createPerformanceRequest(10L);

        when(performanceRepository.findById(performanceId)).thenReturn(Optional.empty());

        assertThrows(PerformanceNotFoundException.class,
                () -> performanceService.updatePerformance(matchId, performanceId, performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    @Test
    public void updatePerformance_shouldThrowPerformanceDoesNotBelongToMatch() {
        Match match = createMatch(1L);
        Performance performance = createPerformance(2L, match);
        Long incorrectMatchId = 3L;
        PerformanceRequest performanceRequest = createPerformanceRequest(10L);

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));

        assertThrows(PerformanceDoesNotBelongToMatchException.class,
                () -> performanceService.updatePerformance(incorrectMatchId, performance.getId(), performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    @Test
    public void updatePerformance_shouldThrowHeroNotFound() {
        Match match = createMatch(1L);
        Performance performance = createPerformance(2L, match);
        Long heroId = 10L;
        PerformanceRequest performanceRequest = createPerformanceRequest(heroId);

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));
        when(heroRepository.findById(heroId)).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class,
                () -> performanceService.updatePerformance(match.getId(), performance.getId(), performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    // ===================== addPerformance =====================

    @Test
    public void addPerformance_shouldAddPerformance() {
        Match match = createMatch(1L);
        Hero hero = createHero(10L);
        PerformanceRequest performanceRequest = createPerformanceRequest(hero.getId());

        when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));
        when(heroRepository.findById(hero.getId())).thenReturn(Optional.of(hero));
        when(performanceRepository.save(any(Performance.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Performance result = performanceService.addPerformance(match.getId(), performanceRequest);

        assertEquals(match, result.getMatch());
        assertEquals(hero, result.getHero());
        assertEquals(performanceRequest.getEliminations(), result.getEliminations());
        assertEquals(performanceRequest.getAssists(), result.getAssists());
        assertEquals(performanceRequest.getDeaths(), result.getDeaths());
        assertEquals(performanceRequest.getTimePlayed(), result.getTimePlayed());

        verify(matchRepository).findById(match.getId());
        verify(heroRepository).findById(hero.getId());
        verify(performanceRepository).save(any(Performance.class));
    }

    @Test
    public void addPerformance_shouldThrowMatchNotFound() {
        Long matchId = 1L;
        PerformanceRequest performanceRequest = createPerformanceRequest(10L);

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(MatchNotFoundException.class,
                () -> performanceService.addPerformance(matchId, performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    @Test
    public void addPerformance_shouldThrowHeroNotFound() {
        Match match = createMatch(1L);
        Long heroId = 10L;
        PerformanceRequest performanceRequest = createPerformanceRequest(heroId);

        when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));
        when(heroRepository.findById(heroId)).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class,
                () -> performanceService.addPerformance(match.getId(), performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    // ===================== findByMatchId =====================

    @Test
    public void findByMatchId_shouldReturnPerformancesOfMatch() {
        Match match = createMatch(1L);
        List<Performance> performances = List.of(
                createPerformance(2L, match),
                createPerformance(3L, match));

        when(matchRepository.existsById(match.getId())).thenReturn(true);
        when(performanceRepository.findByMatchId(match.getId())).thenReturn(performances);

        List<Performance> result = performanceService.findByMatchId(match.getId());

        assertEquals(performances, result);

        verify(performanceRepository).findByMatchId(match.getId());
    }

    @Test
    public void findByMatchId_shouldThrowMatchNotFound() {
        Long matchId = 1L;

        when(matchRepository.existsById(matchId)).thenReturn(false);

        assertThrows(MatchNotFoundException.class,
                () -> performanceService.findByMatchId(matchId));

        verify(performanceRepository, never()).findByMatchId(any());
    }

    // ===================== métodos auxiliares =====================

    private Match createMatch(Long matchId) {
        Match match = new Match();
        match.setId(matchId);
        return match;
    }

    private Hero createHero(Long heroId) {
        Hero hero = new Hero();
        hero.setId(heroId);
        return hero;
    }

    private Performance createPerformance(Long performanceId, Match match) {
        Performance performance = new Performance();
        performance.setId(performanceId);
        performance.setMatch(match);
        return performance;
    }

    private PerformanceRequest createPerformanceRequest(Long heroId) {
        PerformanceRequest performanceRequest = new PerformanceRequest();
        performanceRequest.setHeroId(heroId);
        performanceRequest.setHeroOrder(1);
        performanceRequest.setTimePlayed(Duration.ofMinutes(8));
        performanceRequest.setEliminations(7);
        performanceRequest.setAssists(15);
        performanceRequest.setDeaths(3);
        performanceRequest.setDamageDealt(3456);
        performanceRequest.setHealingDone(8412);
        performanceRequest.setDamageMitigated(0);
        return performanceRequest;
    }
}