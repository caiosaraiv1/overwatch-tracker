package com.caio.overwatch_tracker.hero;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HeroController.class)
public class HeroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HeroRepository heroRepository;

    // ===================== getHeroes (sem filtro) =====================

    @Test
    public void getHeroes_shouldReturn200WithAllHeroes_whenNameIsNotProvided() throws Exception {
        Hero hero1 = createHero(10L, "Ana", Role.SUPPORT);
        Hero hero2 = createHero(11L, "Zarya", Role.TANK);

        when(heroRepository.findAll()).thenReturn(List.of(hero1, hero2));

        mockMvc.perform(get("/heroes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(hero1.getId()))
                .andExpect(jsonPath("$[0].name").value(hero1.getName()))
                .andExpect(jsonPath("$[0].role").value("SUPPORT"))
                .andExpect(jsonPath("$[1].id").value(hero2.getId()))
                .andExpect(jsonPath("$[1].name").value(hero2.getName()))
                .andExpect(jsonPath("$[1].role").value("TANK"));

        verify(heroRepository).findAll();
        verify(heroRepository, never()).findByNameContainingIgnoreCase(any());
    }

    @Test
    public void getHeroes_shouldReturn200WithEmptyList_whenNoHeroesExist() throws Exception {
        when(heroRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/heroes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(heroRepository).findAll();
        verify(heroRepository, never()).findByNameContainingIgnoreCase(any());
    }

    // ===================== getHeroes (com filtro por nome) =====================

    @Test
    public void getHeroes_shouldReturn200WithMatchingHeroes_whenNameMatches() throws Exception {
        Hero hero = createHero(10L, "Ana", Role.SUPPORT);
        String nameQuery = "ana";

        when(heroRepository.findByNameContainingIgnoreCase(nameQuery)).thenReturn(List.of(hero));

        mockMvc.perform(get("/heroes").param("name", nameQuery)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(hero.getId()))
                .andExpect(jsonPath("$[0].name").value(hero.getName()))
                .andExpect(jsonPath("$[0].role").value("SUPPORT"));

        verify(heroRepository).findByNameContainingIgnoreCase(nameQuery);
        verify(heroRepository, never()).findAll();
    }

    @Test
    public void getHeroes_shouldReturn200WithEmptyList_whenNoHeroMatchesName() throws Exception {
        String nameQuery = "xyz";

        when(heroRepository.findByNameContainingIgnoreCase(nameQuery)).thenReturn(List.of());

        mockMvc.perform(get("/heroes").param("name", nameQuery)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(heroRepository).findByNameContainingIgnoreCase(nameQuery);
        verify(heroRepository, never()).findAll();
    }

    // ===================== métodos auxiliares =====================

    private Hero createHero(Long id, String name, Role role) {
        Hero hero = new Hero();
        hero.setId(id);
        hero.setName(name);
        hero.setRole(role);
        return hero;
    }
}