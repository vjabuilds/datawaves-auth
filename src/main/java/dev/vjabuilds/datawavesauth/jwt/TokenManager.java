package dev.vjabuilds.datawavesauth.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenManager {
    private static final long TTL = 24*60*60;
    @Value("${secret_salt}")
    private String secret_salt;

    private UserDetailsService userDetailsService;

    public TokenManager(@Lazy UserDetailsService uds)
    {
        this.userDetailsService = uds;
    }

    public String generateJwtToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList()));
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * TTL))
            .signWith(SignatureAlgorithm.HS512, secret_salt).compact();
    }

    public Boolean validateJwtToken(String token)
    {
        Claims claims = Jwts.parser().setSigningKey(secret_salt).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        boolean expired = claims.getExpiration().before(new Date());

        Set<String> roles = userDetails.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toSet());
        Set<String> token_roles = ((List<String>)claims.get("roles")).stream().collect(Collectors.toSet());
        return username.equals(userDetails.getUsername()) && !expired && roles.equals(token_roles);
    }

     public String getUSername(String token)
     {
        return Jwts.parser().setSigningKey(secret_salt).parseClaimsJws(token).getBody().getSubject();
     }
}
