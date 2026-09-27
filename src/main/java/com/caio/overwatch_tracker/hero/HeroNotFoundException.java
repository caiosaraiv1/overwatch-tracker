package com.caio.overwatch_tracker.hero;

public class HeroNotFoundException extends RuntimeException {

    private final Long heroId;

    public HeroNotFoundException(Long heroId) {
        super("Hero ID " + heroId + " not found.");
        this.heroId = heroId;
    }

    public Long getHeroId() {
        return heroId;
    }
}
