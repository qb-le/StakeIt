package com.stakeit;

import com.stakeit.Repo.BetRepository;
import com.stakeit.service.BetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BetServiceTest {

    @Mock
    private BetRepository betRepository;

    @InjectMocks
    private BetService betService;

    @Test
    void joinBet_WhenUserIsCreator_ShouldThrowException() {
        Integer betId = 1;
        Integer userId = 5;
        Integer selectedOptionId = 2;

        when(betRepository.getBetCreatorId(betId))
                .thenReturn(userId);

        assertThrows(ResponseStatusException.class, () -> {
            betService.joinBet(betId, userId, selectedOptionId);
        });
    }

    @Test
    void joinBet_WhenUserAlreadyJoined_ShouldThrowException() {
        Integer betId = 1;
        Integer userId = 5;
        Integer selectedOptionId = 2;

        when(betRepository.getBetCreatorId(betId))
                .thenReturn(99);

        when(betRepository.hasUserJoinedBet(userId, betId))
                .thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            betService.joinBet(betId, userId, selectedOptionId);
        });
    }
}
