package com.caio.overwatch_tracker.performance;

import com.caio.overwatch_tracker.hero.Hero;
import com.caio.overwatch_tracker.hero.HeroNotFoundException;
import com.caio.overwatch_tracker.hero.Role;
import com.caio.overwatch_tracker.match.MapType;
import com.caio.overwatch_tracker.match.Match;
import com.caio.overwatch_tracker.match.MatchNotFoundException;
import com.caio.overwatch_tracker.match.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PerformanceController.class)
public class PerformanceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private PerformanceService performanceService;

    private static final String VALID_PERFORMANCE_JSON = """
        {
          "heroId": 10,
          "heroOrder": 1,
          "timePlayed": "PT8M",
          "eliminations": 7,
          "assists": 15,
          "deaths": 3,
          "damageDealt": 3456,
          "healingDone": 8412,
          "damageMitigated": 0
        }
        """;

    // ===================== addPerformance =====================

    @Test
    public void addPerformance_shouldReturn201WithPerformance() throws Exception {
        Hero hero = createHero(10L, "Ana", Role.SUPPORT);
        Match match = createMatch(20L, MapType.ESCORT, Result.VICTORY);
        Performance performance = createPerformance(1L, match, hero, 7);

        when(performanceService.addPerformance(eq(match.getId()), any(PerformanceRequest.class)))
                .thenReturn(performance);

        mockMvc.perform(post("/matches/{id}/performances", match.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_PERFORMANCE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(performance.getId()))
                .andExpect(jsonPath("$.eliminations").value(performance.getEliminations()))
                .andExpect(jsonPath("$.hero.id").value(hero.getId()))
                .andExpect(jsonPath("$.hero.name").value(hero.getName()))
                .andExpect(jsonPath("$.hero.role").value("SUPPORT"));

        verify(performanceService).addPerformance(eq(match.getId()), any(PerformanceRequest.class));
    }

    @Test
    public void addPerformance_shouldReturn404_whenMatchNotFound() throws Exception {
        Long matchId = 20L;

        when(performanceService.addPerformance(eq(matchId), any(PerformanceRequest.class)))
                .thenThrow(new MatchNotFoundException(matchId));

        mockMvc.perform(post("/matches/{id}/performances", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_PERFORMANCE_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Match ID 20 not found."))
                .andExpect(jsonPath("$.path").value("/matches/20/performances"));
    }

    @Test
    public void addPerformance_shouldReturn404_whenHeroNotFound() throws Exception {
        Long heroId = 10L;
        Long matchId = 20L;

        when(performanceService.addPerformance(eq(matchId), any(PerformanceRequest.class)))
                .thenThrow(new HeroNotFoundException(heroId));

        mockMvc.perform(post("/matches/{id}/performances", matchId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_PERFORMANCE_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Hero ID 10 not found."))
                .andExpect(jsonPath("$.path").value("/matches/20/performances"));
    }

    // ===================== getPerformancesByMatchId =====================

    @Test
    public void getPerformances_shouldReturn200WithPerformances() throws Exception {
        Hero hero1 = createHero(10L, "Ana", Role.SUPPORT);
        Hero hero2 = createHero(11L, "Zarya", Role.TANK);
        Match match = createMatch(20L, MapType.ESCORT, Result.VICTORY);
        Performance performance1 = createPerformance(1L, match, hero1, 7);
        Performance performance2 = createPerformance(2L, match, hero2, 16);

        when(performanceService.findByMatchId(match.getId())).thenReturn(List.of(performance1, performance2));

        mockMvc.perform(get("/matches/{id}/performances", match.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(performance1.getId()))
                .andExpect(jsonPath("$[0].eliminations").value(performance1.getEliminations()))
                .andExpect(jsonPath("$[0].hero.id").value(hero1.getId()))
                .andExpect(jsonPath("$[0].hero.name").value(hero1.getName()))
                .andExpect(jsonPath("$[0].hero.role").value("SUPPORT"))
                .andExpect(jsonPath("$[1].id").value(performance2.getId()))
                .andExpect(jsonPath("$[1].eliminations").value(performance2.getEliminations()))
                .andExpect(jsonPath("$[1].hero.id").value(hero2.getId()))
                .andExpect(jsonPath("$[1].hero.name").value(hero2.getName()))
                .andExpect(jsonPath("$[1].hero.role").value("TANK"));
    }

    @Test
    public void getPerformances_shouldReturn404_whenMatchNotFound() throws Exception {
        Long matchId = 20L;

        when(performanceService.findByMatchId(matchId)).thenThrow(new MatchNotFoundException(matchId));

        mockMvc.perform(get("/matches/{id}/performances", matchId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Match ID 20 not found."))
                .andExpect(jsonPath("$.path").value("/matches/20/performances"));
    }

    // ===================== métodos auxiliares =====================

    private Performance createPerformance(Long id, Match match, Hero hero, int eliminations) {
        Performance performance = new Performance();
        performance.setMatch(match);
        performance.setHero(hero);
        performance.setId(id);
        performance.setEliminations(eliminations);
        return performance;
    }

    private Match createMatch(Long id, MapType mapType, Result result) {
        Match match = new Match();
        match.setId(id);
        match.setMapType(mapType);
        match.setMatchResult(result);
        return match;
    }

    private Hero createHero(Long id, String name, Role role) {
        Hero hero = new Hero();
        hero.setId(id);
        hero.setName(name);
        hero.setRole(role);
        return hero;
    }
}
