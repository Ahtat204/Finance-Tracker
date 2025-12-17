package org.asue24.financetrackerbackend.services.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service implementation for managing JSON Web Tokens (JWT).
 * This service provides utility methods for generating, parsing, and validating tokens * used for stateless authentication.
 * It utilizes a secret key to sign tokens * ensuring their authenticity and integrity.
 */

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt_secret}")
    private String JwtSecret;

    /**
     * Generates a standard JWT for a specific user email.
     *
     * @param email The user's email address to be set as the token subject. * @return A signed JWT string.
     */
    @Override
    public String generateJwt(String email) {
        var claims = new HashMap<String, Object>();
        return createJwt(claims, email);
    }

    /**
     * Creates a JWT with specific claims and a subject. * Currently sets an expiration time of 30 minutes from the time of issuance.
     *
     * @param claims Map of custom claims to include in the payload. * @param email The subject (email) of the token. * @return A compacted, signed JWT.
     */
    @Override
    public String createJwt(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * @return
     */
    @Override
    public Key getSignKey() {
        var keyBytes = Decoders.BASE64.decode(JwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * @param token
     * @return
     */
    @Override
    public String extractemail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * @param token
     * @return
     */
    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific piece of information (claim) from the token payload.
     *
     * @param <T>            The type of the claim being extracted. * @param token The JWT string.
     * @param claimsResolver A function to map the claims to the desired value.
     * @return The extracted claim value.
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * @param token
     * @return
     */
    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * @param token
     * @return
     */
    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the authenticity and freshness of a token.
     * Checks if the email in the token matches the user's email and ensures the token has not expired.
     *
     * @param token   The JWT string to validate.
     * @param authDto The user details to validate against.
     * @return true if the token is valid and active, false otherwise.
     */
    @Override
    public Boolean validateToken(String token, UserRequestDto authDto) {
        var email = extractemail(token);
        return (email.equals(authDto.getEmail()) && !isTokenExpired(token));
    }
}
