package com.stakeit.api.controller;

import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.entity.BetEntity;
import com.stakeit.service.BetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Bets")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;
    private static final int PAGE_SIZE = 6;

    @PostMapping("/CreateBet")
    public CreateBetResponse createBet(
            @RequestBody CreateBetRequest request,
            @RequestParam Integer gamblerId
    ) {
        return betService.createBet(request, gamblerId);
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
    public String joinBet(@RequestParam Integer betId, @RequestParam Integer userId, @RequestParam Integer selectedOptionId) {
        return betService.joinBet(betId, userId, selectedOptionId);
    }

    @GetMapping("/ReadBet")
    public BetEntity readBet(@RequestParam Integer betId) {
        return betService.readBet(betId);
    }

    @GetMapping("/GetBetsPerPage")
    public ResponseEntity<Map<String, Object>> getBets(@RequestParam(name = "page", defaultValue = "1") Integer pageNr
    ) {
        List<BetEntity> bets = betService.readBetsPage(pageNr);

        int totalBets = betService.countOpenBets();
        int totalPages = (int) Math.ceil((double) totalBets / PAGE_SIZE);

        return ResponseEntity.ok(Map.of(
                "bets", bets,
                "totalPages", totalPages
        ));
    }
}