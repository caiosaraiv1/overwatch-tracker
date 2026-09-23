package com.caio.overwatch_tracker.performance;


import com.caio.overwatch_tracker.hero.Hero;
import com.caio.overwatch_tracker.match.Match;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Duration;

@Entity
@Table(
        name = "performance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"match_id", "hero_id", "hero_order"})
        }
)
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Match match;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Hero hero;

    @Column(nullable = false)
    private Integer heroOrder;

    @Column(nullable = false)
    private Duration timePlayed;

    @Column(nullable = false)
    private Integer eliminations;

    @Column(nullable = false)
    private Integer assists;

    @Column(nullable = false)
    private Integer deaths;

    @Column(nullable = false)
    private Integer damageDealt;

    @Column(nullable = false)
    private Integer healingDone;

    @Column(nullable = false)
    private Integer damageMitigated;

    public Performance() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
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
