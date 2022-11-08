package dev.vjabuilds.datawavesauth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.vjabuilds.datawavesauth.models.DatawavesUser;
import dev.vjabuilds.datawavesauth.models.Role;
import dev.vjabuilds.datawavesauth.repositories.RolesRepository;
import dev.vjabuilds.datawavesauth.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RolesService {
    
    private RolesRepository rolesRepository;
    private UserRepository userRepository;

    public Role createRole(String name)
    {   
        return rolesRepository.save(new Role(null, name, List.of()));
    }

    public void addRole(String username, String roleName)
    {
        Role r = rolesRepository.findByName(roleName);
        DatawavesUser dwu = userRepository.findByEmail(username);
        dwu.getRoles().add(r);
        userRepository.save(dwu);
    }
}
