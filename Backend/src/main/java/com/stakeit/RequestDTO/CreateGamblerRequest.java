package com.stakeit.RequestDTO;

import lombok.Data;

@Data
public class CreateGamblerRequest {
    private String name;
    private String email;
    private String password;
}
