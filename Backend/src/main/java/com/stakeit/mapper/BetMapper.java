package com.stakeit.mapper;

import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.entity.BetEntity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class BetMapper {

    public BetEntity toEntity(CreateBetRequest request) {
        BetEntity entity = new BetEntity();

        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setBetPrice(request.getBetPrice());

        OffsetDateTime betEndsAt = request.getBetEndsAt()
                .atZone(ZoneId.systemDefault())
                .toOffsetDateTime();

        entity.setBetEndsAt(betEndsAt);

        return entity;
    }
}