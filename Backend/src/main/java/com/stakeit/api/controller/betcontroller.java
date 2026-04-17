package com.stakeit.api.controller;

import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.entity.BetEntity;
import com.stakeit.service.BetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping
    public BetEntity createBet(@RequestBody CreateBetRequest request) {
        return betService.createBet(request);
    }

    @GetMapping("OwnBets")
    public List<BetEntity> readOwnBets(Integer userId) {
        return betService.readOwnBets(userId);
    }

    @GetMapping("AllBets")
    public List<BetEntity> readBets() {
        return  betService.readBets();
    }


}