package com.stakeit.ResponseDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class CreateBetResponse {
    private String title;
    private String description;
    private BigDecimal betPrice;
    private OffsetDateTime betEndsAt;
    private String name;

    public CreateBetResponse(String title, String description, BigDecimal betPrice, OffsetDateTime betEndsAt, String name) {
        this.title = title;
        this.description = description;
        this.betPrice = betPrice;
        this.betEndsAt = betEndsAt;
        this.name = name;
    }
}
