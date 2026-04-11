package com.stakeit.entity;

@Entity
public class BetEntity{
    @Id
    private int id;
    private int createdBy;
    private double totalBetAmount;
    private int TotalGamblersJoined;
}
