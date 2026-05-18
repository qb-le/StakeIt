package com.stakeit.service;

import com.stakeit.Repo.BetRepository;
import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.entity.BetEntity;
import com.stakeit.mapper.BetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository repository;
    private final BetMapper betMapper;

    public CreateBetResponse createBet(CreateBetRequest request, Integer gamblerId) {
         BetEntity betEntity = betMapper.toEntity(request);
        return repository.createBet(betEntity, gamblerId);
    }

    public List<BetEntity> readBets() {
        repository.closeExpiredBets();
        return repository.readAllBets();
    }

    public List<BetEntity> readOwnBets(Integer createdBy) {
        return repository.readOwnBets(createdBy);
    }

    public List<BetEntity> readJoinedBets(Integer userId) {
        return repository.readJoinedBets(userId);
    }

    public String joinBet(Integer betId, Integer userId) {
        return repository.joinBet(betId, userId);
    }

    public BetEntity readBet(Integer betId) {
        return repository.readBet(betId);
    }
}