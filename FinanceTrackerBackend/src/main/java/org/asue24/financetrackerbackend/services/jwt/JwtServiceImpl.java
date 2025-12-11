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

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt_secret}")
    private String JwtSecret;

    /**
     * @param email
     * @return
     */
    @Override
    public String generateJwt(String email) {
        var claims = new HashMap<String, Object>();
        return createJwt(claims, email);
    }

    /**
     * @param claims
     * @param email
     * @return
     */
    @Override
    public String createJwt(Map<String, Object> claims, String email) {
        return Jwts.builder().setClaims(claims).
                setSubject(email)
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
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
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
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
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
     * @param token
     * @param authDto
     * @return
     */
    @Override
    public Boolean validateToken(String token, UserRequestDto authDto) {
        var email = extractemail(token);
        return (email.equals(authDto.getEmail()) && !isTokenExpired(token));
    }
}
