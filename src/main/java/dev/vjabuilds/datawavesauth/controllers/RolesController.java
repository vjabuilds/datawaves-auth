package dev.vjabuilds.datawavesauth.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.vjabuilds.datawavesauth.models.Role;
import dev.vjabuilds.datawavesauth.services.RolesService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@AllArgsConstructor
@Log
public class RolesController {
    private RolesService rolesService;
    
    private record RoleCreationModel(String name){
    }

    @PostMapping("/create_role")
    @Secured("admin")
    private ResponseEntity<String> createRole(@RequestBody RoleCreationModel model)
    {
        Role r = rolesService.createRole(model.name);
        log.info(model.name);
        log.info(model.name());
        return ResponseEntity.created(URI.create("/roles/" + r.getRole_id())).body("Created the role!");
    }

    
    private record AddRoleModel(String username, String role_name){
    }

    @PostMapping("/add_role")
    @Secured("admin")
    private ResponseEntity<String> addRole(@RequestBody AddRoleModel model)
    {
        rolesService.addRole(model.username, model.role_name);
        return ResponseEntity.ok("Added the role!");
    }   
}
