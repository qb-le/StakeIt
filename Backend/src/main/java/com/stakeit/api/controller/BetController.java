package com.stakeit.api.controller;

import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.entity.BetEntity;
import com.stakeit.service.BetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping("/CreateBet")
    public ResponseEntity<?> createBet(
            @RequestBody CreateBetRequest request,
            Authentication authentication
    ) {
        Integer gamblerId = (Integer) authentication.getPrincipal();

        return ResponseEntity.ok(betService.createBet(request, gamblerId));
    }

    @GetMapping("/OwnBets")
    public List<BetEntity> readOwnBets(@RequestParam Integer userId) {
        return betService.readOwnBets(userId);
    }

    @GetMapping("/AllBets")
    public List<BetEntity> readBets() {
        return betService.readBets();
    }

    @GetMapping("/JoinedBets")
    public List<BetEntity> readJoinedBets(@RequestParam Integer userId) {
        return betService.readJoinedBets(userId);
    }

    @PostMapping("/JoinBet")
    public String joinBet(
            @RequestParam Integer betId,
            @RequestParam Integer userId
    ) {
        return betService.joinBet(betId, userId);
    }

    @GetMapping("/ReadBet")
    public BetEntity readBet(@RequestParam Integer betId) {
        return betService.readBet(betId);
    }
}