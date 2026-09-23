package com.caio.overwatch_tracker.statistic;

public class StatisticResponse {

    private String heroName;
    private Double winRate;
    private Integer totalGamesPlayed;
    private Integer totalWins;
    private Integer totalDefeats;
    private Integer totalDraws;
    private Double avgEliminations;
    private Double avgAssists;
    private Double avgDeaths;
    private Double avgDamageDealt;
    private Double avgHealingDone;
    private Double avgDamageMitigated;

    public StatisticResponse() {
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Integer getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(Integer totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public Integer getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(Integer totalWins) {
        this.totalWins = totalWins;
    }

    public Integer getTotalDefeats() {
        return totalDefeats;
    }

    public void setTotalDefeats(Integer totalDefeats) {
        this.totalDefeats = totalDefeats;
    }

    public Integer getTotalDraws() {
        return totalDraws;
    }

    public void setTotalDraws(Integer totalDraws) {
        this.totalDraws = totalDraws;
    }

    public Double getAvgEliminations() {
        return avgEliminations;
    }

    public void setAvgEliminations(Double avgEliminations) {
        this.avgEliminations = avgEliminations;
    }

    public Double getAvgAssists() {
        return avgAssists;
    }

    public void setAvgAssists(Double avgAssists) {
        this.avgAssists = avgAssists;
    }

    public Double getAvgDeaths() {
        return avgDeaths;
    }

    public void setAvgDeaths(Double avgDeaths) {
        this.avgDeaths = avgDeaths;
    }

    public Double getAvgDamageDealt() {
        return avgDamageDealt;
    }

    public void setAvgDamageDealt(Double avgDamageDealt) {
        this.avgDamageDealt = avgDamageDealt;
    }

    public Double getAvgHealingDone() {
        return avgHealingDone;
    }

    public void setAvgHealingDone(Double avgHealingDone) {
        this.avgHealingDone = avgHealingDone;
    }

    public Double getAvgDamageMitigated() {
        return avgDamageMitigated;
    }

    public void setAvgDamageMitigated(Double avgDamageMitigated) {
        this.avgDamageMitigated = avgDamageMitigated;
    }
}
