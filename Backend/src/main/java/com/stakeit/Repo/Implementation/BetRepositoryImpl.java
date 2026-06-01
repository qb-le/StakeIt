package com.stakeit.Repo.Implementation;

import com.stakeit.Repo.BetRepository;
import com.stakeit.ResponseDTO.CreateBetResponse;
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

    public List<BetEntity> readJoinedBets(Integer userId) {
        return dsl.select(
                        BET.ID,
                        BET.CREATED_BY,
                        BET.TITLE,
                        BET.DESCRIPTION,
                        BET.BET_PRICE,
                        BET.CREATED_AT,
                        BET.BET_ENDS_AT,
                        BET.STATUS
                )
                .from(JOINED_BET)
                .join(BET).on(JOINED_BET.BET_ID.eq(BET.ID))
                .where(JOINED_BET.GAMBLER_ID.eq(userId))
                .orderBy(BET.CREATED_AT.desc())
                .fetchInto(BetEntity.class);
    }
    public void joinBet(Integer gamblerId, Integer betId, Integer selectedOptionId) {
        dsl.insertInto(JOINED_BET)
                .set(JOINED_BET.GAMBLER_ID, gamblerId)
                .set(JOINED_BET.BET_ID, betId)
                .set(JOINED_BET.SELECTED_OPTION_ID, selectedOptionId)
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

    public void createBetOptions(Integer betId, List<String> options) {
        var inserts = options.stream()
                .map(option -> dsl.insertInto(BET_OPTION)
                        .set(BET_OPTION.BET_ID, betId)
                        .set(BET_OPTION.OPTION_TEXT, option))
                .toList();

        dsl.batch(inserts).execute();
    }

    public Integer getBetCreatorId(Integer betId) {
        return dsl.select(BET.CREATED_BY)
                .from(BET)
                .where(BET.ID.eq(betId))
                .fetchOneInto(Integer.class);
    }
}