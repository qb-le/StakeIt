package com.stakeit.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class Gambler {
    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private String refreshToken;
    private OffsetDateTime refreshTokenExpiry;
    private OffsetDateTime createdAt;
    private BigDecimal walletBalance;
}
