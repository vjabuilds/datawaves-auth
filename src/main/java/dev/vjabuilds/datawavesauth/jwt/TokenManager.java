package dev.vjabuilds.datawavesauth.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenManager implements Serializable {
    private static final long serialVersionUID = 12345L;
    private static final long TTL = 24*60*60;
    @Value("${secret_salt}")
    private String secret_salt;

    public String generateJwtToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * TTL))
            .signWith(SignatureAlgorithm.HS512, secret_salt).compact();
    }

    public Boolean validateJwtToken(String token, UserDetails userDetails)
    {
        Claims claims = Jwts.parser().setSigningKey(secret_salt).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        boolean expired = claims.getExpiration().before(new Date());
        return username.equals(userDetails.getUsername()) && !expired;
    }
}
