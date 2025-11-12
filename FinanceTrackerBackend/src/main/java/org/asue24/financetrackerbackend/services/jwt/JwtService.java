package org.asue24.financetrackerbackend.services.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import org.asue24.financetrackerbackend.dto.AuthDto;

public interface JwtService {

    String generateJwt(String email);
    String createJwt(Map<String, Object> claims, String email);
    Key getSignKey();
    public String extractemail(String token);
    Date extractExpiration(String token);
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    Claims extractAllClaims(String token);
    Boolean isTokenExpired(String token);
    public Boolean validateToken(String token, AuthDto authDto);
}
