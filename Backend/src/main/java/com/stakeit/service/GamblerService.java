package com.stakeit.service;

import com.stakeit.Repo.GamblerRepository;
import com.stakeit.RequestDTO.CreateGamblerRequest;
import com.stakeit.RequestDTO.LoginRequest;
import com.stakeit.ResponseDTO.CreateGamblerResponse;
import com.stakeit.ResponseDTO.LoginResponse;
import com.stakeit.entity.BetEntity;
import com.stakeit.entity.Gambler;
import com.stakeit.mapper.BetMapper;
import com.stakeit.mapper.GamblerMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class GamblerService {
    private final GamblerRepository gamblerRepository;
    private final GamblerMapper gamblerMapper;
    private final JWTService jwtService;

    public CreateGamblerResponse CreateGambler(CreateGamblerRequest request) {
        if (!gamblerRepository.checkIfGamblerExists(request.getEmail())) {
            String passwordHash = BcryptHasher.hashPassword(request.getPassword());
            Gambler gambler = gamblerMapper.ToEntity(request, passwordHash);

            gamblerRepository.createGambler(gambler);

            return new CreateGamblerResponse(
                    gambler,
                    "Gambler created successfully"
            );
        } else {
            return new CreateGamblerResponse(
                    null,
                    "Gambler with this email already exists"
            );
        }
    }

    public LoginResponse LoginGambler(LoginRequest request) {

        Gambler gambler = gamblerRepository.findByEmail(request.getEmail());

        if (gambler == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        if (!BCrypt.checkpw(request.getPassword(), gambler.getPasswordHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        String accessToken = jwtService.generateAccessToken(gambler);
        String refreshToken = jwtService.generateRefreshToken();
        OffsetDateTime refreshTokenExpiry = jwtService.generateRefreshTokenExpiry();

        gamblerRepository.saveGamblerToken(
                gambler.getId(),
                refreshToken,
                refreshTokenExpiry
        );

        gambler.setPasswordHash(null);

        return new LoginResponse(
                gambler.getId(),
                gambler.getEmail(),
                accessToken,
                gambler.getWalletBalance(),
                "Login successful",
                gambler.getName()
        );
    }

}
