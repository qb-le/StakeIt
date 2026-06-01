package com.stakeit.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BetOptions {
    private Integer id;
    private Integer betId;
    private String optionText;
    private OffsetDateTime createdAt;
}
