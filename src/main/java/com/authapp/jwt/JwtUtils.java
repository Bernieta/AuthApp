package com.authapp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.user.generator}")
    private String jwtUserGenerator;

    public String generateToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);

        String email = authentication.getPrincipal().toString();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withIssuer(jwtUserGenerator)
                .withSubject(email)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtUserGenerator)
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Invalid token, unauthorized");
        }
    }

    public String extractClaimSubject(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim extractSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }
}
