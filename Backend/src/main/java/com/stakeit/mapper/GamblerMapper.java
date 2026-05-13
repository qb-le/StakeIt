package com.stakeit.mapper;

import com.stakeit.RequestDTO.CreateGamblerRequest;
import com.stakeit.RequestDTO.LoginRequest;
import com.stakeit.entity.Gambler;
import org.springframework.stereotype.Component;

@Component
public class GamblerMapper {
    public Gambler ToEntity (CreateGamblerRequest request, String password) {
        Gambler gambler = new Gambler();
        gambler.setName(request.getName());
        gambler.setEmail(request.getEmail());
        gambler.setPasswordHash(password);
        return gambler;
    }

    public Gambler ToEntity (LoginRequest request, Integer id) {
        Gambler gambler = new Gambler();
        gambler.setId(id);
        gambler.setEmail(request.getEmail());
        return gambler;
    }
}
