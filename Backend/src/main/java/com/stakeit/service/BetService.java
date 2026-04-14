package com.stakeit.service;

import com.stakeit.Repo.BetRepository;
import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.entity.BetEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository repository;

    public BetEntity createBet(CreateBetRequest request) {
        BetEntity entityRequest = new BetEntity();
        entityRequest.setCreatedBy(request.getCreatedBy());
        entityRequest.setTitle(request.getTitle());
        entityRequest.setDescription(request.getDescription());
        entityRequest.setBetPrice(request.getBetPrice());

        return repository.createBet(entityRequest);
    }
}