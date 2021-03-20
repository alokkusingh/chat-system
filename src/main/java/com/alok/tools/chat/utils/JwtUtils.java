package com.alok.tools.chat.utils;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;


@Component
public class JwtUtils {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("{security.jwt.token.secret-key}")
    private String secret;

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(
                getAllClaimsFromToken(token)
        );
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }
}
