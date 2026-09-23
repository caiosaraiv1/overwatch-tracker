package com.caio.overwatch_tracker.hero;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HeroController {

    private final HeroRepository heroRepository;

    public HeroController(HeroRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    @GetMapping("/heroes")
    public ResponseEntity<List<Hero>> getHeroes() {
        return ResponseEntity.ok(heroRepository.findAll());
    }
}
