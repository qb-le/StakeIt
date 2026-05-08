package com.stakeit.Repo;

import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.entity.BetEntity;

import java.util.List;

public interface BetRepository {
    BetEntity createBet(BetEntity request);
    List<BetEntity> readAllBets();
    List<BetEntity> readOwnBets(Integer CreatedBy);
//    BetEntity readJoinedBets(Integer userId);
//    String joinBet(Integer betId, Integer userId);

}
