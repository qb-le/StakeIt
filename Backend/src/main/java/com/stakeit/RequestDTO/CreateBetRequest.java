package com.stakeit.RequestDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateBetRequest {
    private String title;
    private String description;
    private BigDecimal betPrice;
    private LocalDateTime betEndsAt;
}