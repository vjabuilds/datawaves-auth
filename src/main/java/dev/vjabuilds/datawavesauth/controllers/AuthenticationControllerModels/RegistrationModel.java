package dev.vjabuilds.datawavesauth.controllers.AuthenticationControllerModels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationModel {
    private String email;
    private String password;
    private String name;
    private String last_name;

}
