package dev.vjabuilds.datawavesauth.controllers.AuthenticationControllerModels;

import lombok.Data;

@Data
public class LoginRequestModel {
    private String email;
    private String password;
}