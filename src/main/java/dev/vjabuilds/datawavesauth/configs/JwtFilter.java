package dev.vjabuilds.datawavesauth.configs;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.vjabuilds.datawavesauth.jwt.TokenManager;
import dev.vjabuilds.datawavesauth.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Component
@Log
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private TokenManager tokenManager;
    private UserRepository userRepository;

    private static final String PREFIX = "Bearer ";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization").substring(PREFIX.length());
            
            if(!tokenManager.validateJwtToken(token)){
                log.info("The provided information was invalid");
                throw new BadCredentialsException("The provided token is not valid!");
            }
            else
            {
                log.info("User successfully logged in!");
                var user = userRepository.findByEmail(tokenManager.getUsername(token));
                var authorities = user.getRoles().stream().map(x -> new SimpleGrantedAuthority(x.getName())).collect(Collectors.toList());
                log.info("User has authorities " + authorities.toString());
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(tokenManager.getUsername(token), token, authorities)
                );
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
