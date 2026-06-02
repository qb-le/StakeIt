package com.stakeit.Repo.Implementation;

import com.stakeit.Repo.BetRepository;
import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.ResponseDTO.ReadJoinedBetsResponse;
import com.stakeit.entity.BetEntity;
import com.stakeit.entity.BetOptions;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

import static com.stakeit.jooq.Tables.*;
import static com.stakeit.jooq.Tables.GAMBLER;

@Repository
public class BetRepositoryImpl implements BetRepository {

    private final DSLContext dsl;

    public BetRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<BetEntity> readAllBets() {
        return dsl.selectFrom(BET)
                .where(BET.STATUS.eq("OPEN"))
                .and(BET.BET_ENDS_AT.gt(OffsetDateTime.now()))
                .orderBy(BET.CREATED_AT.desc())
                .fetchInto(BetEntity.class);
    }

    public List<BetEntity> readBetsPage(Integer page) {
        Integer pageSize = 6;
        Integer offset = (page - 1) * pageSize;

        return dsl.selectFrom(BET)
                .where(BET.STATUS.eq("OPEN"))
                .and(BET.BET_ENDS_AT.gt(OffsetDateTime.now()))
                .orderBy(BET.CREATED_AT.desc())
                .limit(pageSize)
                .offset(offset)
                .fetchInto(BetEntity.class);
    }

    public Integer countOpenBets() {
        return dsl.fetchCount(
                dsl.selectFrom(BET)
                        .where(BET.STATUS.eq("OPEN"))
                        .and(BET.BET_ENDS_AT.gt(OffsetDateTime.now()))
        );
    }

    public void closeExpiredBets() {
        dsl.update(BET)
                .set(BET.STATUS, "CLOSED")
                .where(BET.STATUS.eq("OPEN"))
                .and(BET.BET_ENDS_AT.lessOrEqual(OffsetDateTime.now()))
                .execute();
    }

    public List<BetEntity> readOwnBets(Integer userId) {
        return dsl.selectFrom(BET)
                .where(BET.CREATED_BY.eq(userId))
                .orderBy(BET.CREATED_AT.desc())
                .fetchInto(BetEntity.class);
    }

    public CreateBetResponse createBet(BetEntity request, Integer gamblerId) {
        BetEntity createdBet = dsl.insertInto(BET)
                .set(BET.CREATED_BY, gamblerId)
                .set(BET.TITLE, request.getTitle())
                .set(BET.DESCRIPTION, request.getDescription())
                .set(BET.BET_PRICE, request.getBetPrice())
                .set(BET.BET_ENDS_AT, request.getBetEndsAt())
                .set(BET.STATUS, "PENDING_PAYMENT")
                .returning()
                .fetchOneInto(BetEntity.class);

        if (createdBet == null) {
            throw new RuntimeException("Failed to create bet");
        }

        String creatorName = dsl.select(GAMBLER.NAME)
                .from(GAMBLER)
                .where(GAMBLER.ID.eq(gamblerId))
                .fetchOneInto(String.class);

        return new CreateBetResponse(
                createdBet.getId(),
                createdBet.getTitle(),
                createdBet.getDescription(),
                createdBet.getBetPrice(),
                createdBet.getBetEndsAt(),
                creatorName,
                createdBet.getStatus()
        );
    }

    public List<ReadJoinedBetsResponse> readJoinedBets(Integer userId) {
        return dsl.select(
                        BET.ID.as("id"),
                        BET.CREATED_BY.as("createdBy"),
                        BET.TITLE.as("title"),
                        BET.DESCRIPTION.as("description"),
                        BET.BET_PRICE.as("betPrice"),
                        BET.CREATED_AT.as("createdAt"),
                        BET.BET_ENDS_AT.as("betEndsAt"),
                        BET.STATUS.as("status"),

                        JOINED_BET.SELECTED_OPTION_ID.as("selectedOptionId"),
                        BET_OPTION.OPTION_TEXT.as("selectedOption")
                )
                .from(JOINED_BET)
                .join(BET).on(JOINED_BET.BET_ID.eq(BET.ID))
                .join(BET_OPTION).on(JOINED_BET.SELECTED_OPTION_ID.eq(BET_OPTION.ID))
                .where(JOINED_BET.GAMBLER_ID.eq(userId))
                .orderBy(BET.CREATED_AT.desc())
                .fetchInto(ReadJoinedBetsResponse.class);
    }

    public void joinBet(Integer gamblerId, Integer betId, Integer selectedOptionId) {
        dsl.insertInto(JOINED_BET)
                .set(JOINED_BET.GAMBLER_ID, gamblerId)
                .set(JOINED_BET.BET_ID, betId)
                .set(JOINED_BET.SELECTED_OPTION_ID, selectedOptionId)
                .set(JOINED_BET.RESULT, "ONGOING")
                .execute();
    }

    public BetEntity readBet(Integer betId) {
        BetEntity bet = dsl.selectFrom(BET)
                .where(BET.ID.eq(betId))
                .fetchOneInto(BetEntity.class);

        List<BetOptions> options = dsl.selectFrom(BET_OPTION)
                .where(BET_OPTION.BET_ID.eq(betId))
                .orderBy(BET_OPTION.ID.asc())
                .fetchInto(BetOptions.class);

        bet.setBetOptions(options);
        return bet;
    }

    public void updateBetStatus(Integer betId, String status) {
        dsl.update(BET)
                .set(BET.STATUS, status)
                .where(BET.ID.eq(betId))
                .execute();
    }

    public List<BetOptions> createBetOptions(Integer betId, List<String> options) {
        return options.stream()
                .map(option -> dsl.insertInto(BET_OPTION)
                        .set(BET_OPTION.BET_ID, betId)
                        .set(BET_OPTION.OPTION_TEXT, option)
                        .returning()
                        .fetchOneInto(BetOptions.class))
                .toList();
    }

    public Integer getBetCreatorId(Integer betId) {
        return dsl.select(BET.CREATED_BY)
                .from(BET)
                .where(BET.ID.eq(betId))
                .fetchOneInto(Integer.class);
    }

    public boolean hasUserJoinedBet(Integer userId, Integer betId) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(JOINED_BET)
                        .where(JOINED_BET.GAMBLER_ID.eq(userId))
                        .and(JOINED_BET.BET_ID.eq(betId))
        );
    }

    public Integer countWins(Integer userId) {
        return dsl.selectCount()
                .from(JOINED_BET)
                .where(JOINED_BET.GAMBLER_ID.eq(userId))
                .and(JOINED_BET.RESULT.eq("WIN"))
                .fetchOne(0, Integer.class);
    }

    public Integer countFinishedJoinedBets(Integer userId) {
        return dsl.selectCount()
                .from(JOINED_BET)
                .join(BET).on(JOINED_BET.BET_ID.eq(BET.ID))
                .where(JOINED_BET.GAMBLER_ID.eq(userId))
                .and(BET.STATUS.eq("CLOSED"))
                .fetchOne(0, Integer.class);
    }
}