package com.stakeit.service;

import com.stakeit.Repo.BetRepository;
import com.stakeit.RequestDTO.CreateBetRequest;
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

    public BetEntity createBet(CreateBetRequest request) {
         BetEntity betEntity = betMapper.toEntity(request);
        return repository.createBet(betEntity);
    }

    public List<BetEntity> readBets() {
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
}