package com.stakeit.api.controller;

import com.stakeit.RequestDTO.CreateGamblerRequest;
import com.stakeit.RequestDTO.LoginRequest;
import com.stakeit.ResponseDTO.CreateGamblerResponse;
import com.stakeit.ResponseDTO.LoginResponse;
import com.stakeit.service.GamblerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Auth")
@RequiredArgsConstructor
public class GamblerController {
    private final GamblerService gamblerservice;

    @PostMapping("/CreateGambler")
    public CreateGamblerResponse CreateGambler(@RequestBody CreateGamblerRequest request) {
        return gamblerservice.CreateGambler(request);
    }

    @PostMapping("/Login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return gamblerservice.LoginGambler(request);
    }
}
