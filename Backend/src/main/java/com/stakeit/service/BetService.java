package com.stakeit.service;

import com.stakeit.Repo.BetRepository;
import com.stakeit.RequestDTO.CreateBetRequest;
import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.ResponseDTO.ReadJoinedBetsResponse;
import com.stakeit.entity.BetEntity;
import com.stakeit.entity.BetOptions;
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

    public CreateBetResponse createBet(CreateBetRequest request, Integer gamblerId, Integer CreatorChoiceIndex) {
        System.out.println("CREATE BET START");
        System.out.println("gamblerId = " + gamblerId);

        List<String> options = cleanOptions(request.getBetOptions());

        if (options.size() < 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A bet must have at least 2 options"
            );
        }

        if (CreatorChoiceIndex == null || CreatorChoiceIndex < 0 ||  CreatorChoiceIndex >= options.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Creator choice is invalid"
            );
        }

        BetEntity betEntity = betMapper.toEntity(request);
        betEntity.setStatus("PENDING_PAYMENT");

        CreateBetResponse savedBet = repository.createBet(betEntity, gamblerId);

        List<BetOptions> createdOptions = repository.createBetOptions(
                savedBet.getId(),
                options
        );

        BetOptions creatorSelectedOption = createdOptions.get(CreatorChoiceIndex);

        repository.joinBet(
                gamblerId,
                savedBet.getId(),
                creatorSelectedOption.getId()
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

    public List<ReadJoinedBetsResponse> readJoinedBets(Integer userId) {
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

    public double calculateWinRate(Integer userId) {
        Integer wins = repository.countWins(userId);
        Integer finishedBets = repository.countFinishedJoinedBets(userId);

        if (finishedBets == null || finishedBets == 0) {
            return 0;
        }

        return (wins * 100.0) / finishedBets;
    }
}