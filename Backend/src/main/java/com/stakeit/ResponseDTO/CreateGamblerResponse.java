package com.stakeit.ResponseDTO;

import com.stakeit.entity.Gambler;
import lombok.Data;

@Data
public class CreateGamblerResponse {
    private Gambler gambler;
    private String message;

    public CreateGamblerResponse(Gambler gambler, String message) {
        this.gambler = gambler;
        this.message = message;
    }
}
