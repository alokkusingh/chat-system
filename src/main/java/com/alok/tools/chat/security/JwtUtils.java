package com.alok.tools.chat.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.function.Function;


@Component
public class JwtUtils {

    @Value("${security.jwt.token.secret-key}")
    private String secret;

    public UsernamePasswordAuthenticationToken getAuthenticatedPrincipal(String token) {
        return new UsernamePasswordAuthenticationToken(
                getUserNameFromToken(token),
                null,
                Collections.singleton((GrantedAuthority) () -> "USER")
        );
    }

    public String getUserNameFromToken(String token) {
        System.out.println("\n-------------------------------------------------------------\n");
        System.out.println("Validating User Token!");
        System.out.println("\n-------------------------------------------------------------\n");
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
