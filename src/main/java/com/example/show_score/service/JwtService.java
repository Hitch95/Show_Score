package com.example.show_score.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtService {

    private static final Duration JWT_TOKEN_VALIDITY = Duration.ofHours(24);

    private final Algorithm hmac512;
    private final JWTVerifier verifier;

    public JwtService(@Value("${JWT_SECRET}") String secret) {
        this.hmac512 = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.hmac512).build();
    }

    public String generateToken(UserDetails userDetails) {
        final Instant now = Instant.now();
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuer("show-score-app")
                .withIssuedAt(now)
                .withExpiresAt(now.plus(JWT_TOKEN_VALIDITY))
                .withClaim("role", userDetails.getAuthorities().iterator().next().getAuthority())
                .sign(this.hmac512);
    }

    public DecodedJWT validateToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException verificationEx) {
            System.out.println("Token invalid: " + verificationEx.getMessage());
            return null;
        }
    }

    public String extractUsername(String token) {
        DecodedJWT jwt = validateToken(token);
        return jwt != null ? jwt.getSubject() : null;
    }
}