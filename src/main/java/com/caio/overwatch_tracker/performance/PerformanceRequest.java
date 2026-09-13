package com.caio.overwatch_tracker.performance;

import java.time.Duration;

public class PerformanceRequest {

    private Long heroId;
    private Integer heroOrder;
    private Duration timePlayed;
    private Integer eliminations;
    private Integer assists;
    private Integer deaths;
    private Integer damageDealt;
    private Integer healingDone;
    private Integer damageMitigated;

    public PerformanceRequest() {
    }

    public Long getHeroId() {
        return heroId;
    }

    public void setHeroId(Long heroId) {
        this.heroId = heroId;
    }

    public Integer getHeroOrder() {
        return heroOrder;
    }

    public void setHeroOrder(Integer heroOrder) {
        this.heroOrder = heroOrder;
    }

    public Duration getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(Duration timePlayed) {
        this.timePlayed = timePlayed;
    }

    public Integer getEliminations() {
        return eliminations;
    }

    public void setEliminations(Integer eliminations) {
        this.eliminations = eliminations;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(Integer damageDealt) {
        this.damageDealt = damageDealt;
    }

    public Integer getHealingDone() {
        return healingDone;
    }

    public void setHealingDone(Integer healingDone) {
        this.healingDone = healingDone;
    }

    public Integer getDamageMitigated() {
        return damageMitigated;
    }

    public void setDamageMitigated(Integer damageMitigated) {
        this.damageMitigated = damageMitigated;
    }
}
