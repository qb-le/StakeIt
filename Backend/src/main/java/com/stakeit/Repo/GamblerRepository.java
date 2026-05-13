package com.stakeit.Repo;

import com.stakeit.RequestDTO.CreateGamblerRequest;
import com.stakeit.entity.Gambler;

import java.time.OffsetDateTime;

public interface GamblerRepository {
    public boolean checkIfGamblerExists(String email);
    public Gambler createGambler(Gambler gambler);
    public Gambler findByEmail(String email);
    public Gambler saveGamblerToken(Integer id, String refreshToken, OffsetDateTime refreshTokenExpiry);
}
