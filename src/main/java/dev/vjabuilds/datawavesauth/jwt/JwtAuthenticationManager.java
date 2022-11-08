package dev.vjabuilds.datawavesauth.jwt;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.vjabuilds.datawavesauth.models.DatawavesUser;
import dev.vjabuilds.datawavesauth.repositories.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JwtAuthenticationManager implements AuthenticationManager {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException 
    {
        String pwd = (String)authentication.getCredentials();
        String email = (String)authentication.getPrincipal();
        DatawavesUser du = userRepository.findByEmail(email);
        if(du == null)
           throw new UsernameNotFoundException("No user with the email " + email);
        if(!passwordEncoder.matches(pwd, du.getPassword()))
            throw new BadCredentialsException("The provided password does not match up with the one in the database");
        List<SimpleGrantedAuthority> roles = du.getRoles().stream().map(x -> new SimpleGrantedAuthority(x.getName()))
            .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(email, pwd, roles);
    }    
}
