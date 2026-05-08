package com.stakeit.Repo;

import com.stakeit.entity.BetEntity;

import java.util.List;

public interface BetRepository {
    BetEntity createBet(BetEntity request);
    List<BetEntity> readAllBets();
    List<BetEntity> readOwnBets(Integer createdBy);
    List<BetEntity> readJoinedBets(Integer userId);
    String joinBet(Integer betId, Integer userId);
}
