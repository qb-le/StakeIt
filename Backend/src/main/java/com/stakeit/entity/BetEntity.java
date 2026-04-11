package com.stakeit.entity;


public class BetEntity{
    private int id;
    private int createdBy;
    private double betPrice;
    private int totalGamblersJoined;

    public BetEntity(Integer id, Integer createdBy, Double totalBetAmount, Integer totalGamblersJoined) {
        this.id = id;
        this.createdBy = createdBy;
        this.betPrice = totalBetAmount;
        this.totalGamblersJoined = totalGamblersJoined;
    }
}


