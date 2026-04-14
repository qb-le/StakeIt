package com.stakeit.Repo;

import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.entity.BetEntity;

public interface BetRepository {
    BetEntity createBet(BetEntity request);
}
