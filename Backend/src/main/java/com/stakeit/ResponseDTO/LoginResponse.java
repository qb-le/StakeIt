package com.stakeit.ResponseDTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoginResponse {
    private Integer id;
    private String email;
    private String accessToken;
    private BigDecimal walletBalance;
    private String message;
    private String name;

    public LoginResponse(Integer id, String email, String accessToken, BigDecimal walletBalance, String message, String name) {
        this.id = id;
        this.email = email;
        this.accessToken = accessToken;
        this.walletBalance = walletBalance;
        this.message = message;
        this.name = name;
    }
}
