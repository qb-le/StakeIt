package com.stakeit.service;

import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.ResponseDTO.CreateBetResponse;

public interface BetService {
    CreateBetResponse CreateBet(CreateBetRequest request);
}
