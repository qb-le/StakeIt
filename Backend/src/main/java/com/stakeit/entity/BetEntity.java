package com.stakeit.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class BetEntity {
    private Integer id;
    private Integer createdBy;
    private String title;
    private String description;
    private BigDecimal betPrice;
    private OffsetDateTime createdAt;
}