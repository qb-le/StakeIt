package com.stakeit.Repo;

import com.stakeit.entity.Gambler;

import java.time.OffsetDateTime;

public interface GamblerRepository {
    boolean checkIfGamblerExists(String email);
    void createGambler(Gambler gambler);
    Gambler findByEmail(String email);
    void saveGamblerToken(Integer id, String refreshToken, OffsetDateTime refreshTokenExpiry);
}
