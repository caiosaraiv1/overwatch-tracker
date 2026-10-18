package com.caio.overwatch_tracker.match;

import com.caio.overwatch_tracker.hero.HeroNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchController.class)
public class MatchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private MatchService matchService;

    private static final String VALID_MATCH_JSON = """
        {
          "dateTime": "2026-09-30T15:40:00",
          "matchMap": "Rialto",
          "mapType": "ESCORT",
          "matchMode": "QUICK_PLAY",
          "matchResult": "VICTORY",
          "matchScore": "3 vs 0",
          "totalDuration": "PT8M",
          "performanceRequestList": []
        }
        """;

    // ===================== createMatch =====================

    @Test
    public void createMatch_shouldReturn201WithMatch() throws Exception {
        Match match = createMatch(1L, MapType.ESCORT, Result.VICTORY);

        when(matchService.createMatch(any(MatchRequest.class))).thenReturn(match);

        mockMvc.perform(post("/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_MATCH_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(match.getId()))
                .andExpect(jsonPath("$.mapType").value("ESCORT"))
                .andExpect(jsonPath("$.matchResult").value("VICTORY"));
    }

    @Test
    public void createMatch_shouldReturn404_whenHeroNotFound() throws Exception {
        when(matchService.createMatch(any(MatchRequest.class))).thenThrow(new HeroNotFoundException(10L));

        mockMvc.perform(post("/matches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_MATCH_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Hero ID 10 not found."))
                .andExpect(jsonPath("$.path").value("/matches"));
    }

    private Match createMatch(Long id, MapType mapType, Result result) {
        Match match = new Match();
        match.setId(id);
        match.setMapType(mapType);
        match.setMatchResult(result);
        return match;
    }
}
