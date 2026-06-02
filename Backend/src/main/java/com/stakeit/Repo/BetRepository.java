package com.stakeit.Repo;

import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.ResponseDTO.ReadJoinedBetsResponse;
import com.stakeit.entity.BetEntity;
import com.stakeit.entity.BetOptions;

import java.util.List;

public interface BetRepository {
    void closeExpiredBets();
    CreateBetResponse createBet(BetEntity request, Integer gamblerId);
    List<BetEntity> readAllBets();
    List<BetEntity> readBetsPage(Integer page);
    Integer countOpenBets();
    List<BetEntity> readOwnBets(Integer createdBy);
    List<ReadJoinedBetsResponse> readJoinedBets(Integer userId);
    void joinBet(Integer gamblerId, Integer betId, Integer selectedOptionId);
    BetEntity readBet(Integer betId);
    void updateBetStatus(Integer betId, String status);
    List<BetOptions> createBetOptions(Integer betId, List<String> options);
    Integer getBetCreatorId(Integer betId);
    boolean hasUserJoinedBet(Integer userId, Integer betId);
    Integer countWins(Integer userId);
    Integer countFinishedJoinedBets(Integer userId);

}
