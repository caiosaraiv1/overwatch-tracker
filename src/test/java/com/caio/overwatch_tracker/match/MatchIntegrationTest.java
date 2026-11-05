package com.caio.overwatch_tracker.match;

import com.caio.overwatch_tracker.TestcontainersConfiguration;
import com.caio.overwatch_tracker.hero.HeroRepository;
import com.caio.overwatch_tracker.performance.PerformanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class MatchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private HeroRepository heroRepository;

    @Test
    public void createMatch_shouldNotPersistAnything_whenHeroDoesNotExist() throws Exception {
        long matchBefore = matchRepository.count();
        long performanceBefore = performanceRepository.count();

        String json = """
        {
          "dateTime": "2026-11-03T20:00:00",
          "matchMap": "Kings Row",
          "mapType": "HYBRID",
          "matchMode": "COMPETITIVE",
          "matchResult": "VICTORY",
          "matchScore": "3 vs 1",
          "totalDuration": "PT12M",
          "performanceRequestList": [
            {
              "heroId": 50,
              "heroOrder": 1,
              "timePlayed": "PT6M",
              "eliminations": 8,
              "assists": 18,
              "deaths": 3,
              "damageDealt": 4200,
              "healingDone": 9500,
              "damageMitigated": 0
            },
            {
              "heroId": 999,
              "heroOrder": 2,
              "timePlayed": "PT6M",
              "eliminations": 5,
              "assists": 10,
              "deaths": 2,
              "damageDealt": 3000,
              "healingDone": 4000,
              "damageMitigated": 0
            }
          ]
        }
        """;

        mockMvc.perform(post("/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("999")));

        assertEquals(matchBefore, matchRepository.count());
        assertEquals(performanceBefore, performanceRepository.count());
    }

    @Test
    public void createMatch_shouldBeReflectedInStatistics_whenMatchIsCreated() throws Exception {
        Long heroId = heroRepository.findByNameContainingIgnoreCase("Zenyatta").get(0).getId();

        String json = """
        {
          "dateTime": "2026-11-03T20:00:00",
          "matchMap": "Ilios",
          "mapType": "CONTROL",
          "matchMode": "COMPETITIVE",
          "matchResult": "VICTORY",
          "matchScore": "2 vs 0",
          "totalDuration": "PT10M",
          "performanceRequestList": [
            {
              "heroId": %d,
              "heroOrder": 1,
              "timePlayed": "PT10M",
              "eliminations": 10,
              "assists": 20,
              "deaths": 4,
              "damageDealt": 5000,
              "healingDone": 7000,
              "damageMitigated": 0
            }
          ]
        }
        """.formatted(heroId);

        mockMvc.perform(post("/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/statistics/heroes/{id}", heroId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.heroName").value("Zenyatta"))
                .andExpect(jsonPath("$.winRate").value(1.0))
                .andExpect(jsonPath("$.totalGamesPlayed").value(1));
    }
}
