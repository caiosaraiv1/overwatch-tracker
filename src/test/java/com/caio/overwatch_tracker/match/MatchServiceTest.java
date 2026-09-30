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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void shouldCreateMatch() {
        Hero hero = new Hero();
        hero.setId(1L);
        hero.setName("Ana");
        hero.setRole(Role.SUPPORT);

        PerformanceRequest performanceRequest = new PerformanceRequest();
        performanceRequest.setHeroId(1L);
        performanceRequest.setHeroOrder(1);
        performanceRequest.setTimePlayed(Duration.ofMinutes(8));
        performanceRequest.setEliminations(7);
        performanceRequest.setAssists(15);
        performanceRequest.setDeaths(3);
        performanceRequest.setDamageDealt(3456);
        performanceRequest.setHealingDone(8412);
        performanceRequest.setDamageMitigated(0);

        MatchRequest matchRequest = new MatchRequest();
        matchRequest.setDateTime(LocalDateTime.of(2026, 9, 30, 15, 40));
        matchRequest.setMatchMap("Rialto");
        matchRequest.setMapType(MapType.ESCORT);
        matchRequest.setMatchMode(MatchMode.QUICK_PLAY);
        matchRequest.setMatchResult(Result.VICTORY);
        matchRequest.setMatchScore("3 vs 0");
        matchRequest.setTotalDuration(Duration.ofMinutes(8));

        List<PerformanceRequest> performanceRequestList = new ArrayList<>();
        performanceRequestList.add(performanceRequest);

        matchRequest.setPerformanceRequestList(performanceRequestList);

        when(heroRepository.findById(1L)).thenReturn(Optional.of(hero));

        Match savedMatch = new Match();
        savedMatch.setId(1L);

        when(matchRepository.save(any(Match.class))).thenReturn(savedMatch);

        Performance savedPerformance = new Performance();
        savedPerformance.setId(1L);

        when(performanceRepository.save(any(Performance.class))).thenReturn(savedPerformance);

        Match result = matchService.createMatch(matchRequest);

        verify(matchRepository).save(any(Match.class));
        verify(heroRepository).findById(1L);
        verify(performanceRepository).save(any(Performance.class));

        assertEquals(1L, result.getId());
    }

    @Test
    public void shouldThrowHeroNotFoundException() {
        PerformanceRequest performanceRequest = new PerformanceRequest();
        performanceRequest.setHeroId(1L);
        performanceRequest.setHeroOrder(1);
        performanceRequest.setTimePlayed(Duration.ofMinutes(8));
        performanceRequest.setEliminations(7);
        performanceRequest.setAssists(15);
        performanceRequest.setDeaths(3);
        performanceRequest.setDamageDealt(3456);
        performanceRequest.setHealingDone(8412);
        performanceRequest.setDamageMitigated(0);

        MatchRequest matchRequest = new MatchRequest();
        matchRequest.setDateTime(LocalDateTime.of(2026, 9, 30, 15, 40));
        matchRequest.setMatchMap("Rialto");
        matchRequest.setMapType(MapType.ESCORT);
        matchRequest.setMatchMode(MatchMode.QUICK_PLAY);
        matchRequest.setMatchResult(Result.VICTORY);
        matchRequest.setMatchScore("3 vs 0");
        matchRequest.setTotalDuration(Duration.ofMinutes(8));

        List<PerformanceRequest> performanceRequestList = new ArrayList<>();
        performanceRequestList.add(performanceRequest);

        matchRequest.setPerformanceRequestList(performanceRequestList);

        when(heroRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(HeroNotFoundException.class, () -> matchService.createMatch(matchRequest)) ;
    }
}
