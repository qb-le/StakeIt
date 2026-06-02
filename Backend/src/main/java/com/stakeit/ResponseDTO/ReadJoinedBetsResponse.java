package com.stakeit.ResponseDTO;

import com.stakeit.entity.BetEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ReadJoinedBetsResponse {
    private Integer id;
    private Integer createdBy;
    private String title;
    private String description;
    private BigDecimal betPrice;
    private OffsetDateTime createdAt;
    private OffsetDateTime betEndsAt;
    private String status;

    private Integer selectedOptionId;
    private String selectedOption;
}
