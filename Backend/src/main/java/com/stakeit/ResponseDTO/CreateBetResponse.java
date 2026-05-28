package com.stakeit.ResponseDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class CreateBetResponse {
    private int id;
    private String title;
    private String description;
    private BigDecimal betPrice;
    private OffsetDateTime betEndsAt;
    private String status;
    private String name;
    private String checkoutUrl;

    public CreateBetResponse(
            Integer id,
            String title,
            String description,
            BigDecimal betPrice,
            OffsetDateTime betEndsAt,
            String name,
            String status
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.betPrice = betPrice;
        this.betEndsAt = betEndsAt;
        this.name = name;
        this.status = status;
    }
}
