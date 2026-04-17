package com.stakeit.repository;

import static com.stakeit.jooq.Tables.BET;

import com.stakeit.Repo.BetRepository;
import com.stakeit.entity.BetEntity;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class BetRepositoryImpl implements BetRepository {

    private final DSLContext dsl;

    public BetRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<BetEntity> readAllBets() {
        return dsl.selectFrom(BET)
                .orderBy(BET.CREATED_AT.desc())
                .fetchInto(BetEntity.class);
    }

    public List<BetEntity> readOwnBets(Integer userId) {
        return dsl.selectFrom(BET)
                .where(BET.CREATED_BY.eq(userId))
                .orderBy(BET.CREATED_AT.desc())
                .fetchInto(BetEntity.class);
    }

    public BetEntity createBet(BetEntity request) {
        return dsl.insertInto(BET)
                .set(BET.CREATED_BY, request.getCreatedBy())
                .set(BET.TITLE, request.getTitle())
                .set(BET.DESCRIPTION, request.getDescription())
                .set(BET.BET_PRICE, request.getBetPrice())
                .set(BET.BET_ENDS_AT, request.getBetEndsAt())
                .returning()
                .fetchOneInto(BetEntity.class);
    }
}