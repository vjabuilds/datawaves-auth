package dev.vjabuilds.datawavesauth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.vjabuilds.datawavesauth.controllers.AuthenticationControllerModels.RegistrationModel;
import dev.vjabuilds.datawavesauth.controllers.AuthenticationControllerModels.ResponseModel;
import dev.vjabuilds.datawavesauth.controllers.AuthenticationControllerModels.LoginRequestModel;
import dev.vjabuilds.datawavesauth.jwt.TokenManager;
import dev.vjabuilds.datawavesauth.services.DatawavesUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@AllArgsConstructor
@Log
public class AuthenticationController {

    DatawavesUserDetailsService userDetailsService;
    AuthenticationManager authenticationManager;
    TokenManager tokenManager;

    @PostMapping("/login")
    public ResponseModel login(@RequestBody LoginRequestModel model) throws Exception
    {
        log.info("logging in user...");
        try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getEmail(), model.getPassword()));
        } catch(BadCredentialsException e) {
            log.severe("Failed to log in user!!!");
            throw new Exception("The requested user has invalid credentials!");
        }

        return new ResponseModel(
            tokenManager.generateJwtToken(
                userDetailsService.loadUserByUsername(model.getEmail())
            )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationModel model) throws Exception
    {
        try {
            this.userDetailsService.registerNewUserAccount(model);
        } catch(Exception ex) {
            log.severe("Failed to register user, got error " + ex.toString());
            return ResponseEntity.badRequest().body("Failed to register the user with given parameters");
        }
        return ResponseEntity.ok("Registered the user successfully!");
    }

}
