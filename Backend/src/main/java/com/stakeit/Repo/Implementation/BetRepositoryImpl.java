package com.stakeit.Repo.Implementation;

import com.stakeit.Repo.BetRepository;
import com.stakeit.ResponseDTO.CreateBetResponse;
import com.stakeit.entity.BetEntity;
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
                .fetchInto(BetEntity.class);
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
                .set(BET.STATUS, "OPEN")
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
                createdBet.getTitle(),
                createdBet.getDescription(),
                createdBet.getBetPrice(),
                createdBet.getBetEndsAt(),
                creatorName);
    }

    public List<BetEntity> readJoinedBets(Integer userId) {
        return dsl.select(
                        BET.ID,
                        BET.CREATED_BY,
                        BET.TITLE,
                        BET.DESCRIPTION,
                        BET.BET_PRICE,
                        BET.CREATED_AT,
                        BET.BET_ENDS_AT
                )
                .from(JOINED_USER)
                .join(BET).on(JOINED_USER.BET_ID.eq(BET.ID))
                .where(JOINED_USER.USER_ID.eq(userId))
                .orderBy(BET.CREATED_AT.desc())
                .fetchInto(BetEntity.class);
    }
    public String joinBet(Integer betId, Integer userId) {
        dsl.insertInto(JOINED_USER)
                .set(JOINED_USER.BET_ID, betId)
                .set(JOINED_USER.USER_ID, userId)
                .execute();

        return "User joined bet successfully";
    }

    public BetEntity readBet(Integer betId) {
        return dsl.selectFrom(BET)
                .where(BET.ID.eq(betId))
                .fetchOneInto(BetEntity.class);
    }
}