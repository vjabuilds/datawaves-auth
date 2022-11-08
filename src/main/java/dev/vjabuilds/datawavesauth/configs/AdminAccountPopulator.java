package dev.vjabuilds.datawavesauth.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import dev.vjabuilds.datawavesauth.controllers.AuthenticationControllerModels.RegistrationModel;
import dev.vjabuilds.datawavesauth.models.Role;
import dev.vjabuilds.datawavesauth.repositories.UserRepository;
import dev.vjabuilds.datawavesauth.services.DatawavesUserDetailsService;
import lombok.Data;

@Configuration
@Data
@Order(Ordered.LOWEST_PRECEDENCE)
public class AdminAccountPopulator {

    @Autowired    
    private DatawavesUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Value("${admin.username}")
    private String admin_username;
    @Value("${admin.password}")
    private String admin_password;

    @Bean
    @ConditionalOnProperty(
        value = "admin.populate",
        havingValue = "true",
        matchIfMissing =  false
    )
    CommandLineRunner populate_admin(String... args)
    {
        return x -> {
            var user = userDetailsService.registerNewUserAccount(new RegistrationModel(
                    admin_username, 
                    admin_password, 
                    "admin", 
                    "admin"));
            user.getRoles().add(new Role(null, "admin", List.of()));
            userRepository.save(user);
        };
    }
}
