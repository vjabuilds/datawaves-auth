package dev.vjabuilds.datawavesauth.services;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.vjabuilds.datawavesauth.controllers.AuthenticationControllerModels.RegistrationModel;
import dev.vjabuilds.datawavesauth.models.DatawavesUser;
import dev.vjabuilds.datawavesauth.models.Role;
import dev.vjabuilds.datawavesauth.repositories.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class DatawavesUserDetailsService implements UserDetailsService {

    public UserRepository userRepository;
    public PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        return new User(user.getEmail(), 
                    user.getPassword(), 
                    user.isEnabled(), 
                    true,
                    true, 
                    true, 
                    List.of());
    }

    public DatawavesUser registerNewUserAccount(RegistrationModel model)
    {
        DatawavesUser cu = new DatawavesUser(
            null,
            model.getName(),
            model.getLast_name(),
            model.getEmail(),
            passwordEncoder.encode(model.getPassword()),
            "",
            true,
            new HashSet<Role>()
        );
        return userRepository.save(cu);
    }
    
}
