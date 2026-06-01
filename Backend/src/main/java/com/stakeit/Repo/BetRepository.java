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
    void joinBet(Integer gamblerId, Integer betId, Integer selectedOptionId);
    BetEntity readBet(Integer betId);
    void updateBetStatus(Integer betId, String status);
    void createBetOptions(Integer betId, List<String> options);
    Integer getBetCreatorId(Integer betId);

}
