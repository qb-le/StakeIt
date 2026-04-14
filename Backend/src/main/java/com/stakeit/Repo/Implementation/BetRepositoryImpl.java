package com.stakeit.Repo.Implementation;

import com.stakeit.Repo.BetRepository;
import com.stakeit.entity.BetEntity;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Repository
@RequiredArgsConstructor
public class BetRepositoryImpl implements BetRepository {

    private final DSLContext dsl;

    public BetEntity createBet(BetEntity bet) {
        Record record = dsl.fetchOne("""
            INSERT INTO bet (created_by, title, description, bet_price)
            VALUES (?, ?, ?, ?)
            RETURNING
                id,
                created_by,
                title,
                description,
                bet_price,
                created_at
            """,
                bet.getCreatedBy(),
                bet.getTitle(),
                bet.getDescription(),
                bet.getBetPrice()
        );

        if (record == null) {
            throw new RuntimeException("Failed to create bet");
        }

        BetEntity savedBet = new BetEntity();
        savedBet.setId(record.get("id", Integer.class));
        savedBet.setCreatedBy(record.get("created_by", Integer.class));
        savedBet.setTitle(record.get("title", String.class));
        savedBet.setDescription(record.get("description", String.class));
        savedBet.setBetPrice(record.get("bet_price", BigDecimal.class));
        savedBet.setCreatedAt(record.get("created_at", OffsetDateTime.class));

        return savedBet;
    }
}