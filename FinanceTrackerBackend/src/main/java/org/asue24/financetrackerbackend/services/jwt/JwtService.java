package org.asue24.financetrackerbackend.services.jwt;

import io.jsonwebtoken.Claims;
import org.asue24.financetrackerbackend.dto.UserRequestDto;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String generateJwt(String email,Long Id);

    String createJwt(Map<String, Object> claims, String email,Long Id);

    Key getSignKey();

    String extractemail(String token);

    Date extractExpiration(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    Claims extractAllClaims(String token);

    Boolean isTokenExpired(String token);

    Boolean validateToken(String token, UserRequestDto authDto);

    String extractId(String token);
}
