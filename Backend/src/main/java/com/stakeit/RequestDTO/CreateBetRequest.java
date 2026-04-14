package com.stakeit.RequestDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class CreateBetRequest {
    private Integer createdBy;
    private String title;
    private String description;
    private BigDecimal betPrice;
    private OffsetDateTime createdAt;
}