package com.stakeit.mapper;

import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.RequestDTO.CreateGamblerRequest;
import com.stakeit.entity.BetEntity;
import com.stakeit.entity.Gambler;
import org.springframework.stereotype.Component;

@Component
public class BetMapper {

    public BetEntity toEntity(CreateBetRequest request) {
        BetEntity entity = new BetEntity();
        entity.setCreatedBy(request.getCreatedBy());
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setBetPrice(request.getBetPrice());
        return entity;
    }
}
