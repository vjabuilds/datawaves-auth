package dev.vjabuilds.datawavesauth.jwt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

        String pwd = passwordEncoder.encode((String)authentication.getCredentials());
        String email = (String)authentication.getPrincipal();

        DatawavesUser du = userRepository.findByEmail(email);
        if(du == null)
           throw new UsernameNotFoundException("No user with the email " + email);
        if(du.getPassword() != pwd)
            throw new BadCredentialsException("The provided password does not match the username");
        authentication.setAuthenticated(true);
        return authentication;
    }
    
}
