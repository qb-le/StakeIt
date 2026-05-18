package com.stakeit.Repo;

import com.stakeit.ResponseDTO.CreateGamblerResponse;
import com.stakeit.entity.Gambler;

import java.time.OffsetDateTime;

public interface GamblerRepository {
    boolean checkIfGamblerExists(String email);
    Gambler createGambler(Gambler gambler);
    Gambler findByEmail(String email);
    void saveGamblerToken(Integer id, String refreshToken, OffsetDateTime refreshTokenExpiry);
}
