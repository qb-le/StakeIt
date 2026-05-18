package com.stakeit.Repo.Implementation;

import com.stakeit.Repo.GamblerRepository;
import static com.stakeit.jooq.Tables.GAMBLER;
import static com.stakeit.jooq.tables.Bet.BET;

import com.stakeit.entity.BetEntity;
import com.stakeit.entity.Gambler;
import org.springframework.stereotype.Repository;
import org.jooq.DSLContext;

import java.time.OffsetDateTime;

@Repository
public class GamblerRepositoryImpl implements GamblerRepository {

    private final DSLContext dsl;

    public GamblerRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public boolean checkIfGamblerExists(String email) {
        return dsl.fetchExists(
                dsl.selectFrom(GAMBLER)
                        .where(GAMBLER.EMAIL.equalIgnoreCase(email))
        );
    }

    public Gambler findByEmail(String email) {
        return dsl.selectFrom(GAMBLER)
                .where(GAMBLER.EMAIL.equalIgnoreCase(email))
                .fetchOptional(record -> {
                    Gambler gambler = new Gambler();

                    gambler.setId(record.getId());
                    gambler.setEmail(record.getEmail());
                    gambler.setName(record.getName());
                    gambler.setPasswordHash(record.getPasswordHash());
                    gambler.setWalletBalance(record.getWalletBalance());

                    return gambler;
                })
                .orElse(null);
    }

    public Gambler createGambler(Gambler gambler) {
        return dsl.insertInto(GAMBLER)
                .set(GAMBLER.NAME, gambler.getName())
                .set(GAMBLER.EMAIL, gambler.getEmail())
                .set(GAMBLER.PASSWORD_HASH, gambler.getPasswordHash())
                .returning()
                .fetchOneInto(Gambler.class);
    }

    public void saveGamblerToken(Integer id, String refreshToken, OffsetDateTime refreshTokenExpiry) {
        dsl.update(GAMBLER)
                .set(GAMBLER.REFRESH_TOKEN, refreshToken)
                .set(GAMBLER.REFRESH_TOKEN_EXPIRY, refreshTokenExpiry)
                .where(GAMBLER.ID.eq(id))
                .returning()
                .fetchOne(record -> {
                    Gambler gambler = new Gambler();

                    gambler.setId(record.getId());
                    gambler.setEmail(record.getEmail());
                    gambler.setName(record.getName());
                    gambler.setWalletBalance(record.getWalletBalance());
                    gambler.setCreatedAt(record.getCreatedAt());

                    return gambler;
                });
    }
}
