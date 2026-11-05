package com.caio.overwatch_tracker.hero;

import com.caio.overwatch_tracker.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class HeroRepositoryIntegrationTest {

    @Autowired
    private HeroRepository heroRepository;

    @Test
    public void findByNameContainingIgnoreCase_shouldFindHero_whenCaseDiffers() {
        List<Hero> result = heroRepository.findByNameContainingIgnoreCase("ANA");

        assertEquals(1, result.size());
        assertEquals("Ana", result.get(0).getName());
    }

    @Test
    public void findByNameContainingIgnoreCase_shouldReturnAllMatches_whenNameIsPartial() {
        List<Hero> result = heroRepository.findByNameContainingIgnoreCase("a");

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(h -> h.getName().toLowerCase(Locale.ROOT).contains("a")));
    }

    @Test
    public void findByNameContainingIgnoreCase_shouldReturnEmptyList_whenNoHeroMatches() {
        List<Hero> result = heroRepository.findByNameContainingIgnoreCase("xyz");

        assertTrue(result.isEmpty());
    }
}
