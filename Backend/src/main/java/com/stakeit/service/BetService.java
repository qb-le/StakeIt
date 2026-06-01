package com.stakeit.service;

import com.stakeit.Repo.BetRepository;
import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.entity.BetEntity;
import com.stakeit.mapper.BetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository repository;
    private final BetMapper betMapper;
    private final StripeService stripe;

    public CreateBetResponse createBet(CreateBetRequest request, Integer gamblerId) {
        System.out.println("CREATE BET START");
        System.out.println("gamblerId = " + gamblerId);

        BetEntity betEntity = betMapper.toEntity(request);
        betEntity.setStatus("PENDING_PAYMENT");

        CreateBetResponse savedBet = repository.createBet(betEntity, gamblerId);

        repository.createBetOptions(
                savedBet.getId(),
                cleanOptions(request.getBetOptions())
        );

        try {
            String checkoutUrl = stripe.createCreateBetCheckoutSession(
                    savedBet.getId(),
                    savedBet.getTitle(),
                    savedBet.getBetPrice()
            );

            savedBet.setCheckoutUrl(checkoutUrl);
            return savedBet;

        } catch (Exception e) {
            System.out.println("STRIPE CHECKOUT FAILED");

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not create Stripe checkout session",
                    e
            );
        }
    }

    private List<String> cleanOptions(List<String> options) {
        return options.stream()
                .filter(option -> option != null && !option.isBlank())
                .map(String::trim)
                .toList();
    }

    public List<BetEntity> readBets() {
        repository.closeExpiredBets();
        return repository.readAllBets();
    }

    public List<BetEntity> readBetsPage(Integer page) {
        return repository.readBetsPage(page);
    }

    public Integer countOpenBets() {
        return repository.countOpenBets();
    }

    public List<BetEntity> readOwnBets(Integer createdBy) {
        return repository.readOwnBets(createdBy);
    }

    public List<BetEntity> readJoinedBets(Integer userId) {
        return repository.readJoinedBets(userId);
    }

    public String joinBet(Integer betId, Integer userId, Integer selectedOptionId) {
        Integer creatorId = repository.getBetCreatorId(betId);

        if (creatorId == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Bet not found"
            );
        }

        if (creatorId.equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot join your own bet"
            );
        }

        boolean alreadyJoined = repository.hasUserJoinedBet(userId, betId);

        if (alreadyJoined) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You already joined this bet"
            );
        }

        repository.joinBet(userId, betId, selectedOptionId);

        return "Joined bet successfully";
    }

    public BetEntity readBet(Integer betId) {
        return repository.readBet(betId);
    }

    public void activateBetAfterPayment(Integer betId) {
        repository.updateBetStatus(betId, "OPEN");
    }
}