package dev.vjabuilds.datawavesauth.controllers.AuthenticationControllerModels;

import lombok.Data;

@Data
public class VerificationRequestModel {
    private String email;
    private String password;
}
