package com.stakeit.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class JWTAuthConfig {

    private final JWTAuth jwtAuthenticationFilter;

    public JWTAuthConfig(JWTAuth jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/Auth/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        .requestMatchers("/Bets/GetBetsPerPage").permitAll()
                        .requestMatchers("/Bets/AllBets").permitAll()
                        .requestMatchers("/Bets/ReadBet").permitAll()

                        .requestMatchers("/Bets/CreateBet").authenticated()
                        .requestMatchers("/Bets/JoinBet").authenticated()
                        .requestMatchers("/Bets/OwnBets").authenticated()
                        .requestMatchers("/Bets/JoinedBets").authenticated()

                        .requestMatchers("/Stripe/Webhook").permitAll()


                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}