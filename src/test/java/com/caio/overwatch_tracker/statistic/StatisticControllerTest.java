package com.caio.overwatch_tracker.statistic;

import com.caio.overwatch_tracker.hero.HeroNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatisticController.class)
public class StatisticControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private StatisticService statisticService;

    // ===================== getStatistics =====================

    @Test
    public void getStatistics_shouldReturn200WithStatistics() throws Exception {
        Long heroId = 10L;
        String heroName = "Ana";
        StatisticResponse statisticResponse = createStatisticResponse(heroName, 0.4, 20, 15.0);

        when(statisticService.getStatistics(heroId)).thenReturn(statisticResponse);

        mockMvc.perform(get("/statistics/heroes/{heroId}", heroId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.heroName").value(heroName))
                .andExpect(jsonPath("$.winRate").value(0.4))
                .andExpect(jsonPath("$.totalGamesPlayed").value(20))
                .andExpect(jsonPath("$.avgEliminations").value(15.0));
    }

    @Test
    public void getStatistics_shouldReturn404_whenHeroNotFound() throws Exception {
        Long heroId = 10L;

        when(statisticService.getStatistics(heroId)).thenThrow(new HeroNotFoundException(heroId));

        mockMvc.perform(get("/statistics/heroes/{heroId}", heroId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Hero ID 10 not found."))
                .andExpect(jsonPath("$.path").value("/statistics/heroes/10"));
    }

    // ===================== métodos auxiliares =====================

    private StatisticResponse createStatisticResponse(String heroName, Double winRate,
                                                      Integer totalGamesPlayed, Double avgEliminations) {
        StatisticResponse response = new StatisticResponse();
        response.setHeroName(heroName);
        response.setWinRate(winRate);
        response.setTotalGamesPlayed(totalGamesPlayed);
        response.setAvgEliminations(avgEliminations);
        return response;
    }
}
