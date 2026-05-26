package com.stakeit.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Data
public class BetEntity {
    private Integer id;
    private Integer createdBy;
    private String title;
    private String description;
    private BigDecimal betPrice;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime betEndsAt;


    public void setBetPrice(BigDecimal betPrice) {
        try {
            Objects.requireNonNull(betPrice, "betPrice cannot be null");

            BigDecimal betPriceMin = BigDecimal.ZERO;
            BigDecimal betPriceMax = betPrice.multiply(BigDecimal.valueOf(99.99));

            if (betPrice.compareTo(betPriceMin) < 0) {
                throw new IllegalArgumentException("betPrice cannot be less than 0");
            }
            if (betPrice.compareTo(betPriceMax) > 0) {
                throw new IllegalArgumentException("Bet price cannot be greater than or equal to " + betPriceMax);
            }
            this.betPrice = betPrice;
        }
        catch (Exception e) {
            throw new IllegalArgumentException("");
        }
    }
}