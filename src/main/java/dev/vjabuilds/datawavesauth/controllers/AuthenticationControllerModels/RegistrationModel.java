package dev.vjabuilds.datawavesauth.controllers.AuthenticationControllerModels;

import lombok.Data;

@Data
public class RegistrationModel {
    private String email;
    private String password;
    private String name;
    private String last_name;

}
