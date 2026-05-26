package com.stakeit.Repo;

import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.entity.BetEntity;

import java.util.List;

public interface BetRepository {
    void closeExpiredBets();
    CreateBetResponse createBet(BetEntity request, Integer gamblerId);
    List<BetEntity> readAllBets();
    List<BetEntity> readBetsPage(Integer page);
    Integer countOpenBets();
    List<BetEntity> readOwnBets(Integer createdBy);
    List<BetEntity> readJoinedBets(Integer userId);
    String joinBet(Integer betId, Integer userId);
    BetEntity readBet(Integer betId);

}
