package com.caio.overwatch_tracker.match;

import com.caio.overwatch_tracker.hero.Hero;
import com.caio.overwatch_tracker.hero.HeroNotFoundException;
import com.caio.overwatch_tracker.hero.HeroRepository;
import com.caio.overwatch_tracker.hero.Role;
import com.caio.overwatch_tracker.performance.Performance;
import com.caio.overwatch_tracker.performance.PerformanceRepository;
import com.caio.overwatch_tracker.performance.PerformanceRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private HeroRepository heroRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @InjectMocks
    private MatchService matchService;

    // ===================== createMatch =====================

    @Test
    public void createMatch_shouldCreateMatchWithPerformances() {
        Hero hero = createHero(10L);
        MatchRequest matchRequest = createMatchRequest(List.of(createPerformanceRequest(hero.getId())));

        when(heroRepository.findById(hero.getId())).thenReturn(Optional.of(hero));
        when(matchRepository.save(any(Match.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Match result = matchService.createMatch(matchRequest);

        assertMatchFieldsEqualRequest(matchRequest, result);

        verify(matchRepository).save(any(Match.class));
        verify(heroRepository).findById(hero.getId());
        verify(performanceRepository).save(any(Performance.class));
    }

    @Test
    public void createMatch_shouldThrowHeroNotFound() {
        Long heroId = 10L;
        MatchRequest matchRequest = createMatchRequest(List.of(createPerformanceRequest(heroId)));

        when(heroRepository.findById(heroId)).thenReturn(Optional.empty());

        assertThrows(HeroNotFoundException.class, () -> matchService.createMatch(matchRequest));

        verify(performanceRepository, never()).save(any());
    }

    // ===================== updateMatch =====================

    @Test
    public void updateMatch_shouldUpdateMatch() {
        Match existingMatch = createExistingMatch(1L);
        MatchRequest matchRequest = createMatchRequest(List.of());

        when(matchRepository.findById(existingMatch.getId())).thenReturn(Optional.of(existingMatch));
        when(matchRepository.save(existingMatch)).thenReturn(existingMatch);

        Match result = matchService.updateMatch(existingMatch.getId(), matchRequest);

        assertMatchFieldsEqualRequest(matchRequest, result);

        verify(matchRepository).findById(existingMatch.getId());
        verify(matchRepository).save(existingMatch);
    }

    @Test
    public void updateMatch_shouldThrowMatchNotFound() {
        Long matchId = 1L;
        MatchRequest matchRequest = createMatchRequest(List.of());

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(MatchNotFoundException.class, () -> matchService.updateMatch(matchId, matchRequest));

        verify(matchRepository, never()).save(any());
    }

    // ===================== findAll =====================

    @Test
    public void findAll_shouldReturnAllMatches() {
        List<Match> matches = List.of(createExistingMatch(1L), createExistingMatch(2L));

        when(matchRepository.findAll()).thenReturn(matches);

        List<Match> result = matchService.findAll();

        assertEquals(matches, result);

        verify(matchRepository).findAll();
    }

    // ===================== métodos auxiliares =====================

    private Hero createHero(Long id) {
        Hero hero = new Hero();
        hero.setId(id);
        hero.setName("Ana");
        hero.setRole(Role.SUPPORT);
        return hero;
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

    private MatchRequest createMatchRequest(List<PerformanceRequest> performanceRequests) {
        MatchRequest matchRequest = new MatchRequest();
        matchRequest.setDateTime(LocalDateTime.of(2026, 9, 30, 15, 40));
        matchRequest.setMatchMap("Rialto");
        matchRequest.setMapType(MapType.ESCORT);
        matchRequest.setMatchMode(MatchMode.QUICK_PLAY);
        matchRequest.setMatchResult(Result.VICTORY);
        matchRequest.setMatchScore("3 vs 0");
        matchRequest.setTotalDuration(Duration.ofMinutes(8));
        matchRequest.setPerformanceRequestList(performanceRequests);
        return matchRequest;
    }

    private Match createExistingMatch(Long id) {
        Match match = new Match();
        match.setId(id);
        match.setDateTime(LocalDateTime.of(2026, 9, 1, 20, 0));
        match.setMatchMap("Kings Row");
        match.setMapType(MapType.HYBRID);
        match.setMatchMode(MatchMode.COMPETITIVE);
        match.setMatchResult(Result.DEFEAT);
        match.setMatchScore("1 vs 2");
        match.setTotalDuration(Duration.ofMinutes(12));
        return match;
    }

    private void assertMatchFieldsEqualRequest(MatchRequest expected, Match actual) {
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getMatchMap(), actual.getMatchMap());
        assertEquals(expected.getMapType(), actual.getMapType());
        assertEquals(expected.getMatchMode(), actual.getMatchMode());
        assertEquals(expected.getMatchResult(), actual.getMatchResult());
        assertEquals(expected.getMatchScore(), actual.getMatchScore());
        assertEquals(expected.getTotalDuration(), actual.getTotalDuration());
    }
}