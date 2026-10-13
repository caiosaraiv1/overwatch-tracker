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
        Match match = new Match();
        match.setId(1L);

        Performance performance = new Performance();
        performance.setId(2L);

        performance.setMatch(match);

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));

        performanceService.deletePerformance(match.getId(), performance.getId());

        verify(performanceRepository).delete(performance);
    }

    @Test
    public void deletePerformance_shouldThrowPerformanceNotFoundException() {
        Long performanceId = 1L;
        Long matchId = 2L;

        when(performanceRepository.findById(performanceId)).thenReturn(Optional.empty());

        assertThrows(PerformanceNotFoundException.class, () -> performanceService.deletePerformance(matchId, performanceId));

        verify(performanceRepository, never()).delete(any());
    }

    @Test
    public void deletePerformance_shouldThrowPerformanceDoesNotBelongToMatchException() {
        Match match = new Match();
        match.setId(1L);

        Performance performance = new Performance();
        performance.setId(2L);

        performance.setMatch(match);

        Long incorrectMatchId = 3L;

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));

        assertThrows(PerformanceDoesNotBelongToMatchException.class, () -> performanceService.deletePerformance(incorrectMatchId, performance.getId()));

        verify(performanceRepository, never()).delete(any());
    }

    // ===================== updatePerformance =====================

    @Test
    public void updatePerformance_shouldThrowPerformanceNotFound() {
        Match match = new Match();
        match.setId(1L);

        Performance performance = new Performance();
        performance.setId(2L);

        performance.setMatch(match);

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.empty());

        PerformanceRequest performanceRequest = createPerformanceRequest(1L);

        assertThrows(PerformanceNotFoundException.class, () -> performanceService.updatePerformance(match.getId(), performance.getId(), performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    @Test
    public void updatePerformance_shouldThrowPerformanceDoesNotBelongToMatchException() {
        Match match = new Match();
        match.setId(1L);

        Performance performance = new Performance();
        performance.setId(2L);

        performance.setMatch(match);

        Long incorrectMatchId = 3L;

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));

        PerformanceRequest performanceRequest = createPerformanceRequest(1L);

        assertThrows(PerformanceDoesNotBelongToMatchException.class, () -> performanceService.updatePerformance(incorrectMatchId, performance.getId(), performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    @Test
    public void updatePerformance_shouldThrowHeroNotFoundException() {
        Match match = new Match();
        match.setId(1L);

        Performance performance = new Performance();
        performance.setId(2L);

        performance.setMatch(match);

        Hero hero = new Hero();
        hero.setId(3L);

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));
        when(heroRepository.findById(hero.getId())).thenReturn(Optional.empty());

        PerformanceRequest performanceRequest = createPerformanceRequest(hero.getId());

        assertThrows(HeroNotFoundException.class, () -> performanceService.updatePerformance(match.getId(), performance.getId(), performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    @Test
    public void updatePerformance_shouldUpdatePerformance() {
        Match match = new Match();
        match.setId(1L);

        Hero oldHero = new Hero();
        oldHero.setId(3L);

        Hero newHero = new Hero();
        newHero.setId(4L);

        Performance performance = new Performance();
        performance.setId(2L);

        performance.setMatch(match);
        performance.setHero(oldHero);

        when(performanceRepository.findById(performance.getId())).thenReturn(Optional.of(performance));
        when(heroRepository.findById(newHero.getId())).thenReturn(Optional.of(newHero));

        PerformanceRequest performanceRequest = createPerformanceRequest(newHero.getId());

        when(performanceRepository.save(performance)).thenReturn(performance);

        Performance result = performanceService.updatePerformance(match.getId(), performance.getId(), performanceRequest);

        verify(performanceRepository).save(performance);

        assertEquals(newHero, result.getHero());
    }

    // ===================== addPerformance =====================

    @Test
    public void addPerformance_shouldThrowMatchNotFoundException() {
        Match match = new Match();
        match.setId(1L);

        PerformanceRequest performanceRequest = createPerformanceRequest(2L);

        when(matchRepository.findById(match.getId())).thenReturn(Optional.empty());

        assertThrows(MatchNotFoundException.class, () -> performanceService.addPerformance(match.getId(), performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    @Test
    public void addPerformance_shouldThrowHeroNotFoundException() {
        Match match = new Match();
        match.setId(1L);

        Hero hero = new Hero();
        hero.setId(2L);

        PerformanceRequest performanceRequest = createPerformanceRequest(hero.getId());

        when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));
        when(heroRepository.findById(hero.getId())).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class, () -> performanceService.addPerformance(match.getId(), performanceRequest));

        verify(performanceRepository, never()).save(any());
    }

    @Test
    public void addPerformance_shouldAddPerformance() {
        Match match = new Match();
        match.setId(1L);

        Hero hero = new Hero();
        hero.setId(2L);

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

        verify(performanceRepository).save(any(Performance.class));
        verify(matchRepository).findById(match.getId());
        verify(heroRepository).findById(hero.getId());
    }

    // ===================== findByMatchId =====================

    @Test
    public void findByMatchId_shouldThrowMatchNotFoundException() {
        Match match = new Match();
        match.setId(1L);

        when(matchRepository.existsById(match.getId())).thenReturn(false);

        assertThrows(MatchNotFoundException.class, () -> performanceService.findByMatchId(match.getId()));

        verify(performanceRepository, never()).findByMatchId(any());
    }

    @Test
    public void findByMatchId_shouldFindByMatchId() {
        Match match = new Match();
        match.setId(1L);

        Performance performance = new Performance();
        performance.setMatch(match);

        List<Performance> performances = List.of(performance);

        when(matchRepository.existsById(match.getId())).thenReturn(true);
        when(performanceRepository.findByMatchId(match.getId())).thenReturn(performances);

        List<Performance> result = performanceService.findByMatchId(match.getId());

        assertEquals(performances, result);

        verify(performanceRepository).findByMatchId(match.getId());
    }

    // ===================== métodos auxiliares =====================

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
