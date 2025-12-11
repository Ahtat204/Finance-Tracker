package org.asue24.financetrackerbackend.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtConfig {
    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String email) throws IllegalArgumentException, JWTCreationException {
        return JWT.create().withSubject("User Details").withClaim("email", email).withIssuedAt(new Date()).withIssuer("JOB TRACKER APPLICATION")
                .sign(Algorithm.HMAC256(secret));

    }

    public String validateTokenAndRetrieveSubject(String token) throws
            JWTVerificationException {
        var verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("JOB TRACKER APPLICATION")
                .build();

        var jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}
