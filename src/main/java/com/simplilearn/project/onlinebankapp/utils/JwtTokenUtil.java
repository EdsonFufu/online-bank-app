package com.simplilearn.project.onlinebankapp.utils;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Slf4j
@Component
public class JwtTokenUtil {
    @Value("${app.jwtSecret}")
    private String jwtTokenSecret;
    @Value("${app.jwtExpirationMs}")
    private long jwtTokenExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtTokenSecret)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtTokenSecret)
                    .parseClaimsJws(token);
            return true;
        }catch(UnsupportedJwtException exp) {
            log.error("claimsJws argument does not represent Claims JWS" + exp.getMessage());
        }catch(MalformedJwtException exp) {
            log.error("claimsJws string is not a valid JWS" + exp.getMessage());
        }catch(SignatureException exp) {
            log.error("claimsJws JWS signature validation failed" + exp.getMessage());
        }catch(ExpiredJwtException exp) {
            log.error("Claims has an expiration time before the method is invoked" + exp.getMessage());
        }catch(IllegalArgumentException exp) {
            System.out.println("claimsJws string is null or empty or only whitespace" + exp.getMessage());
        }
        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        Claims claims =Jwts.parser()
                .setSigningKey(jwtTokenSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();

    }
}
