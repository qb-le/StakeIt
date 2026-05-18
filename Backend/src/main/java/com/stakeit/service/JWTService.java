package com.stakeit.service;

import com.stakeit.entity.Gambler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_ACCESS_EXPIRATION_HOURS}")
    private long accessExpirationHours;

    @Value("${JWT_REFRESH_EXPIRATION_DAYS}")
    private long refreshExpirationDays;

    private final SecureRandom secureRandom = new SecureRandom();

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Gambler gambler) {
        Date now = new Date();

        Date expiryDate = new Date(
                now.getTime() + accessExpirationHours * 60 * 60 * 1000
        );

        return Jwts.builder()
                .subject(gambler.getEmail())
                .claim("gamblerId", gambler.getId())
                .claim("email", gambler.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    public OffsetDateTime generateRefreshTokenExpiry() {
        return OffsetDateTime.now().plusDays(refreshExpirationDays);
    }

    public boolean isAccessTokenValid(String accessToken) {
        try {
            Claims claims = extractAllClaims(accessToken);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractEmail(String accessToken) {
        return extractAllClaims(accessToken).getSubject();
    }

    public Integer extractGamblerId(String accessToken) {
        return extractAllClaims(accessToken).get("gamblerId", Integer.class);
    }

    private Claims extractAllClaims(String accessToken) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }
}